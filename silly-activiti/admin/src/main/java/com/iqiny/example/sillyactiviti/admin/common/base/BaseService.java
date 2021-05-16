/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iqiny.example.sillyactiviti.admin.common.utils.Constant;
import com.iqiny.example.sillyactiviti.admin.common.utils.Query;
import com.iqiny.example.sillyactiviti.common.utils.BeanUtils;

import java.util.List;
import java.util.Map;

public class BaseService<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> {

    public PageUtils queryPage(Map<String, Object> params) {
        return queryPage(params, null);
    }

    public PageUtils queryPage(Map<String, Object> params, Class<T> clazz) {
        QueryWrapper<T> qw = makeQueryWrapper(params, clazz);
        IPage<T> page = doQueryPage(makeNewPage(params), qw);
        setRecordInfo(page.getRecords());
        return new PageUtils(page);
    }

    protected IPage<T> makeNewPage(Map<String, Object> params) {
        params.putIfAbsent(Constant.ORDER, Constant.DESC);
        params.putIfAbsent(Constant.ORDER_FIELD, "create_date");
        return new Query<T>().getPage(params);
    }

    protected QueryWrapper<T> makeQueryWrapper(Map<String, Object> params, Class<T> clazz) {
        if (clazz == null || params == null) {
            return new QueryWrapper<>();
        }
        T entity = BeanUtils.mapToBean(params, clazz);
        return makeQueryWrapper(entity);
    }

    protected QueryWrapper<T> makeQueryWrapper(T entity) {
        QueryWrapper<T> qw = new QueryWrapper<>();
        qw.setEntity(entity);
        return qw;
    }


    protected IPage<T> doQueryPage(IPage<T> page, QueryWrapper<T> qw) {
        return baseMapper.selectPage(page, qw);
    }

    /**
     * 子类重写此方法设置分页数据信息
     *
     * @param records
     */
    protected void setRecordInfo(List<T> records) {
    }

    @Override
    public boolean save(T entity) {
        entity.preInsert();
        return super.save(entity);
    }

    @Override
    public boolean updateById(T entity) {
        entity.preUpdate();
        return super.updateById(entity);
    }
}
