/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus
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
public class DefaultSillyQueryUtil implements IQueryUtil {

    /**
     * 当前页码
     */
    protected String pageNo(){
        return "pageNo";
    }

    /**
     * 每页显示记录数
     */
    protected String limit(){
        return "rows";
    }

    /**
     * 排序字段
     */
    protected String orderField(){
        return "sidx";
    }

    /**
     * 排序方式
     */
    protected String orderName(){
        return "sord";
    }

    /**
     * 升序
     */
    protected String asc(){
        return "ASC";
    }

    /**
     * 降序
     */
    protected String desc(){
        return "DESC";
    }

    /**
     * 默认排序字段
     */
    protected String defaultOrderField(){
        return null;
    }


    @Override
    public <T> IPage<T> getPage(Map<String, Object> params) {
        String order = (String) params.get(orderName());
        return this.getPage(params, defaultOrderField(), asc().equalsIgnoreCase(order));
    }

    public <T> IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if (params.get(pageNo()) != null) {
            curPage = Long.parseLong(params.get(pageNo()).toString());
        }
        if (params.get(limit()) != null) {
            limit = Long.parseLong(params.get(limit()).toString());
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);
        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        List<String> orderFields = new ArrayList<>();
        final Object orderFieldObj = params.get(orderField());
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

    private <T> void addOrder(Page<T> page, String orderField, boolean isAsc) {
        if (isAsc) {
            page.addOrder(OrderItem.asc(orderField));
        } else {
            page.addOrder(OrderItem.desc(orderField));
        }
    }

}
