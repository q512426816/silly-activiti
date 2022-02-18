/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti;

import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.core.base.SillyMasterTask;
import org.activiti.engine.impl.db.IbatisVariableTypeHandler;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.impl.variable.VariableType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;


import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * 集成activiti 工作流引擎服务
 */
public class DefaultSillyActivitiEngineService extends BaseSillyActivitiEngineService {

    private final static Log log = LogFactory.getLog(DefaultSillyActivitiEngineService.class);

    private String category;
    private SqlSessionFactory sqlSessionFactory;
    private final String DEFAULT_MYBATIS_MAPPING_FILE = "iqiny/silly/mappings.xml";

    protected SqlSessionFactory initSqlSessionFactory() {
        DataSource dataSource = sillyContext.getBean(usedCategory(), DataSource.class);
        TransactionFactory transactionFactory = sillyContext.getBean(usedCategory(), TransactionFactory.class);
        if (transactionFactory == null) {
            transactionFactory = new SpringManagedTransactionFactory();
        }

        try (InputStream inputStream = ReflectUtil.getResourceAsStream(DEFAULT_MYBATIS_MAPPING_FILE)) {
            Environment environment = new Environment("default", transactionFactory, dataSource);
            Reader reader = new InputStreamReader(inputStream);
            Properties properties = new Properties();
            properties.put("category", category);
            XMLConfigBuilder parser = new XMLConfigBuilder(reader, null, properties);
            Configuration configuration = parser.getConfiguration();
            configuration.setEnvironment(environment);
            configuration.getTypeHandlerRegistry().register(VariableType.class, JdbcType.VARCHAR, new IbatisVariableTypeHandler());
            configuration = parser.parse();
            return new SqlSessionFactoryBuilder().build(configuration);
        } catch (Exception e) {
            throw new SillyException("Error while building ibatis SqlSessionFactory: " + e.getMessage(), e);
        }
    }

    @Override
    public void otherInit() {
        super.otherInit();

        this.sqlSessionFactory = initSqlSessionFactory();
        SillyAssert.notNull(sqlSessionFactory, "sqlSessionFactory 不可为空[需要配置MyBatis]");
    }

    @Override
    public String usedCategory() {
        return category;
    }

    @Override
    public List<SillyMasterTask> getDoingMasterTask(String category, String userId) {
        return getMyDoingMasterTaskId(category, userId, null);
    }

    @Override
    public List<SillyMasterTask> getHistoryMasterTask(String category, String userId) {
        Map<String, Object> param = new HashMap<>();
        param.put("category", category);
        param.put("userId", userId);

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectList(namespace() + "getHistoryMasterTask", param);
        } catch (Exception e) {
            log.warn("数据查询异常" + e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SillyMasterTask> getMyDoingMasterTaskId(String category, String userId, String masterId) {
        Map<String, Object> param = new HashMap<>();
        if (sillyCurrentUserUtil.isAdmin(userId)) {
            userId = null;
        } else {
            // 计算用户所拥有的 流程 GROUP_ID
            Set<String> allGroupId = sillyTaskGroupHandle.getAllGroupId(category, userId);
            param.put("allGroupId", allGroupId);
        }
        param.put("category", category);
        param.put("businessKey", masterId);
        param.put("userId", userId);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectList(namespace() + "getDoingMasterTask", param);
        } catch (Exception e) {
            log.warn("数据查询异常" + e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    private String namespace() {
        return "com.iqiny.silly.core.read.MySillyMasterTask.";
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
