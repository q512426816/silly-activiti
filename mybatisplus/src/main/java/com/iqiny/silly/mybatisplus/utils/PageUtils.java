/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 */
public class PageUtils implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private int records;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int total;
    /**
     * 当前页数
     */
    private int pageNo;
    /**
     * 列表数据
     */
    private List<?> rows;

    /**
     * 分页
     *
     * @param rows     列表数据
     * @param records  总记录数
     * @param pageSize 每页记录数
     * @param pageNo   当前页数
     */
    public PageUtils(List<?> rows, int records, int pageSize, int pageNo) {
        this.rows = rows;
        this.records = records;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.total = (int) Math.ceil((double) records / pageSize);
    }

    /**
     * 分页
     */
    public PageUtils(IPage<?> page) {
        this.rows = page.getRecords();
        this.records = (int) page.getTotal();
        this.pageSize = (int) page.getSize();
        this.pageNo = (int) page.getCurrent();
        this.total = (int) page.getPages();
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
