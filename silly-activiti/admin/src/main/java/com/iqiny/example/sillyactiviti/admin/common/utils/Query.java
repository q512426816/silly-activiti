/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import com.iqiny.example.sillyactiviti.common.utils.StringUtils;
import com.iqiny.example.sillyactiviti.common.xss.SQLFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 查询参数
 *
 *
 */
public class Query<T> {

    public IPage<T> getPage(Map<String, Object> params) {
        String order = (String) params.get(Constant.ORDER);
        return this.getPage(params, null, Constant.ASC.equalsIgnoreCase(order));
    }

    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if (params.get(Constant.PAGE) != null) {
            curPage = Long.parseLong((String) params.get(Constant.PAGE));
        }
        if (params.get(Constant.LIMIT) != null) {
            limit = Long.parseLong((String) params.get(Constant.LIMIT));
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
        params.put(Constant.PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        List<String> orderFields = new ArrayList<>();
        final Object orderFieldObj = params.get(Constant.ORDER_FIELD);
        if (orderFieldObj instanceof String) {
            orderFields.add(SQLFilter.sqlInject((String) orderFieldObj));
        } else if (orderFieldObj instanceof Collection) {
            for (Object filedName : ((Collection) orderFieldObj)) {
                orderFields.add(SQLFilter.sqlInject((String) filedName));
            }
        } else if (orderFieldObj instanceof String[]) {
            for (String filedName : ((String[]) orderFieldObj)) {
                orderFields.add(SQLFilter.sqlInject(filedName));
            }
        }
        
        //前端字段排序
        if (!orderFields.isEmpty()) {
            for (String field : orderFields) {
                String orderField = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field);
                addOrder(page, orderField, isAsc);
            }
            return page;
        }

        //没有排序字段，则不排序
        if (StringUtils.isBlank(defaultOrderField)) {
            return page;
        }

        //默认排序
        addOrder(page, defaultOrderField, isAsc);

        return page;
    }

    private void addOrder(Page<T> page, String orderField, boolean isAsc) {
        if (isAsc) {
            page.addOrder(OrderItem.asc(orderField));
        } else {
            page.addOrder(OrderItem.desc(orderField));
        }
    }

}
