/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.iqiny.silly.common.util.SillyReflectUtil;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.config.property.option.SillyProcessNodeOptionProperty;
import com.iqiny.silly.mybatisplus.baseentity.BaseMySillyMaster;
import com.iqiny.silly.mybatisplus.baseentity.BaseMySillyNode;
import com.iqiny.silly.mybatisplus.baseentity.BaseMySillyVariable;
import com.iqiny.silly.mybatisplus.handle.SillyMasterConvertorReadHandle;
import com.iqiny.silly.mybatisplus.service.BaseMySillyReadService;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;


public class DefaultMySillyReadService<M extends BaseMySillyMaster<M>, N extends BaseMySillyNode<N, V>, V extends BaseMySillyVariable<V>>
        extends BaseMySillyReadService<M, N, V> {

    private String category;

    @Override
    public String usedCategory() {
        return category;
    }

    @Override
    protected IPage<Map<String, Object>> doQueryPage(IPage<Map<String, Object>> page, Map<String, Object> params) {
        QueryWrapper<M> queryWrapper = makeQueryWrapper(params);
        Object queryIds = params.get(queryIdsParamName());
        if (queryIds != null) {
            List<List<String>> partitionMasterIds = (List<List<String>>) queryIds;
            if (!partitionMasterIds.isEmpty()) {
                StringJoiner sqlApply = new StringJoiner(" or ");
                for (List<String> partitionMasterId : partitionMasterIds) {
                    StringJoiner sj = new StringJoiner(",");
                    for (String masterId : partitionMasterId) {
                        sj.add("'" + masterId + "'");
                    }
                    sqlApply.add(" (" + masterIdMapKey() + " in (" + sj + ")) ");
                }
                queryWrapper.apply("(" + sqlApply + ")");
            }
        }

        Map<String, Object> map = new HashMap<>(2);
        map.put(Constants.WRAPPER, queryWrapper);
        map.put("page", page);
        SqlSession sqlSession = SqlHelper.sqlSession(this.masterClass());
        try {
            List<Map<String, Object>> records = new ArrayList<>();
            List<? extends SillyMaster> masterList = sqlSession.selectList(SqlHelper.table(masterClass()).getSqlStatement(SqlMethod.SELECT_PAGE.getMethod()), map);
            SillyMasterConvertorReadHandle convertorReadHandle = sillyContext.getBean(usedCategory(), SillyMasterConvertorReadHandle.class);
            for (SillyMaster master : masterList) {
                Map<String, Object> masterMap = convertorReadHandle.handle(master);
                records.add(masterMap);
            }
            page.setRecords(records);
        } finally {
            SqlSessionUtils.closeSqlSession(sqlSession, GlobalConfigUtils.currentSessionFactory(masterClass()));
        }
        return page;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public List<SillyProcessNodeOptionProperty> option(String taskId, String masterId) {
        List<SillyProcessNodeOptionProperty> list = new ArrayList<>();
        SillyProcessNodeProperty<?> nodeProperty = getNodeProperty(taskId);
        SillyPropertyHandle propertyHandle = newSillyPropertyHandle(masterId, new HashMap<>());
        Map<String, SillyProcessNodeOptionProperty> sillyOption = nodeProperty.getSillyOption();
        Set<Map.Entry<String, SillyProcessNodeOptionProperty>> entries = sillyOption.entrySet();
        for (Map.Entry<String, SillyProcessNodeOptionProperty> entry : entries) {
            SillyProcessNodeOptionProperty value = entry.getValue();
            SillyProcessNodeOptionProperty optionProperty = SillyReflectUtil.newInstance(value.getClass());
            BeanUtils.copyProperties(value, optionProperty);
            optionProperty.setHandle(propertyHandle);
            list.add(optionProperty);
        }
        return list;
    }
}
