/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.group;

import com.iqiny.silly.core.config.property.valuesfield.SillyPropertyValuesField;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

/**
 * 默认 业务分类分组对象 （当无法取到对应分组对象时创建）
 */
public class DefaultSillyTaskCategoryGroup extends BaseSillyTaskCategoryGroup implements SillyPropertyValuesField {

    /**
     * 业务分类
     */
    protected String category;
    /**
     * 变量属性名称
     */
    protected String variableName;
    /**
     * 分组属性名称
     */
    protected String groupName;
    /**
     * 变量表中存储 此分组用户ID 字段属性名称
     */
    protected String userVariableName;


    @Override
    public String usedCategory() {
        return category;
    }

    @Override
    protected String groupUserVariableName() {
        return userVariableName;
    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setUserVariableName(String userVariableName) {
        this.userVariableName = userVariableName;
    }

    @Override
    public String fieldName() {
        return variableName;
    }

    @Override
    public Object value(SillyNodeSourceData data) {
        return keyCovGroupId(data.masterId());
    }
}
