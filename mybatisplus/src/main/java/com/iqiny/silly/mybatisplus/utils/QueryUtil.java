/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import com.iqiny.silly.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 查询参数
 */
public class QueryUtil<T> {

    /**
     * 当前页码
     */
    public static final String PAGE = "pageNo";
    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "rows";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * 排序方式
     */
    public static final String ORDER = "sord";
    /**
     * 升序
     */
    public static final String ASC = "asc";
    /**
     * 降序
     */
    public static final String DESC = "desc";
    

    public IPage<T> getPage(Map<String, Object> params) {
        String order = (String) params.get(ORDER);
        return this.getPage(params, null, ASC.equalsIgnoreCase(order));
    }

    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if (params.get(PAGE) != null) {
            curPage = Long.parseLong(params.get(PAGE).toString());
        }
        if (params.get(LIMIT) != null) {
            limit = Long.parseLong(params.get(LIMIT).toString());
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
        params.put(PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        List<String> orderFields = new ArrayList<>();
        final Object orderFieldObj = params.get(ORDER_FIELD);
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
                if (StringUtils.isNotEmpty(field)) {
                    String orderField = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field);
                    addOrder(page, orderField, isAsc);
                }
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
