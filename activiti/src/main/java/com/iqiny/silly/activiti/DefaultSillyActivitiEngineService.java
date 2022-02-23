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
import com.iqiny.silly.core.base.SillyCategory;
import com.iqiny.silly.core.base.SillyContext;
import com.iqiny.silly.core.read.MySillyMasterTask;
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
public class DefaultSillyActivitiEngineService extends BaseSillyActivitiEngineService implements SillyCategory {

    private final static Log log = LogFactory.getLog(DefaultSillyActivitiEngineService.class);

    private String category;
    private SqlSessionFactory sqlSessionFactory;
    private final String DEFAULT_MYBATIS_MAPPING_FILE = "iqiny/silly/mappings.xml";

    protected void initSqlSessionFactory(SillyContext sillyContext) {
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
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        } catch (Exception e) {
            throw new SillyException("Error while building ibatis SqlSessionFactory: " + e.getMessage(), e);
        }
    }

    @Override
    public void init() {
        SillyContext sillyContext = usedConfig().getSillyContext();
        init(sillyContext);
    }

    public void init(SillyContext sillyContext) {
        initSqlSessionFactory(sillyContext);
        otherInit(sillyContext);
    }

    @Override
    public String usedCategory() {
        return category;
    }

    @Override
    public List<MySillyMasterTask> getDoingMasterTask(String category, String userId, Set<String> allGroupId) {
        return getMyDoingMasterTaskId(category, userId, null, allGroupId);
    }

    @Override
    public List<MySillyMasterTask> getHistoryMasterTask(String category, String userId) {
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
    public List<MySillyMasterTask> getMyDoingMasterTaskId(String category, String userId, String masterId, Set<String> allGroupId) {
        Map<String, Object> param = new HashMap<>();
        param.put("allGroupId", allGroupId);
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
