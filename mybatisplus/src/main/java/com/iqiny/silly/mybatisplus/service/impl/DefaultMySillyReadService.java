/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.iqiny.silly.mybatisplus.baseentity.BaseMySillyMaster;
import com.iqiny.silly.mybatisplus.baseentity.BaseMySillyNode;
import com.iqiny.silly.mybatisplus.baseentity.BaseMySillyVariable;
import com.iqiny.silly.mybatisplus.service.BaseMySillyReadService;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;


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
            page.setRecords(sqlSession.selectList(SqlHelper.table(masterClass()).getSqlStatement(SqlMethod.SELECT_MAPS_PAGE.getMethod()), map));
        } finally {
            SqlSessionUtils.closeSqlSession(sqlSession, GlobalConfigUtils.currentSessionFactory(masterClass()));
        }
        return page;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
