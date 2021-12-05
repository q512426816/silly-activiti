/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring-boot-starter
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.starter;

import com.iqiny.silly.spring.spel.SillySpelPropertyHandle;
import com.iqiny.silly.core.base.SillyProperties;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.property.SillyProcessProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.config.property.impl.DefaultProcessProperty;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.core.service.SillyWriteService;
import com.iqiny.silly.mybatisplus.service.impl.DefaultMySillyReadService;
import com.iqiny.silly.mybatisplus.service.impl.DefaultMySillyWriteService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = StarterSillyProperties.PREFIX)
public class StarterSillyProperties implements SillyProperties {

    public static final String PREFIX = "silly";

    private String processPattern = "classpath*:/silly/*.json";

    private String entityScanPackage;

    private String[] categories;

    private Class<? extends SillyMaster> masterSuperType = SillyMaster.class;

    private Class<? extends SillyNode> nodeSuperType = SillyNode.class;

    private Class<? extends SillyVariable> variableSuperType = SillyVariable.class;

    private Class<? extends SillyResume> resumeSuperType = SillyResume.class;


    private Class<? extends SillyProcessProperty> processPropertyClazz = DefaultProcessProperty.class;

    private Class<? extends SillyPropertyHandle> propertyHandleClazz = SillySpelPropertyHandle.class;

    private Class<? extends SillyReadService> defaultReadServiceClazz = DefaultMySillyReadService.class;

    private Class<? extends SillyWriteService> defaultWriteServiceClazz = DefaultMySillyWriteService.class;

    @Override
    public String getProcessPattern() {
        return processPattern;
    }

    public void setProcessPattern(String processPattern) {
        this.processPattern = processPattern;
    }

    @Override
    public String getEntityScanPackage() {
        return entityScanPackage;
    }

    public void setEntityScanPackage(String entityScanPackage) {
        this.entityScanPackage = entityScanPackage;
    }

    @Override
    public Class<? extends SillyMaster> getMasterSuperType() {
        return masterSuperType;
    }

    public void setMasterSuperType(Class<? extends SillyMaster> masterSuperType) {
        this.masterSuperType = masterSuperType;
    }

    @Override
    public Class<? extends SillyNode> getNodeSuperType() {
        return nodeSuperType;
    }

    public void setNodeSuperType(Class<? extends SillyNode> nodeSuperType) {
        this.nodeSuperType = nodeSuperType;
    }

    @Override
    public Class<? extends SillyVariable> getVariableSuperType() {
        return variableSuperType;
    }

    public void setVariableSuperType(Class<? extends SillyVariable> variableSuperType) {
        this.variableSuperType = variableSuperType;
    }

    @Override
    public Class<? extends SillyResume> getResumeSuperType() {
        return resumeSuperType;
    }

    public void setResumeSuperType(Class<? extends SillyResume> resumeSuperType) {
        this.resumeSuperType = resumeSuperType;
    }

    @Override
    public Class<? extends SillyProcessProperty> getProcessPropertyClazz() {
        return processPropertyClazz;
    }

    public void setProcessPropertyClazz(Class<? extends SillyProcessProperty> processPropertyClazz) {
        this.processPropertyClazz = processPropertyClazz;
    }

    @Override
    public Class<? extends SillyPropertyHandle> getPropertyHandleClazz() {
        return propertyHandleClazz;
    }

    public void setPropertyHandleClazz(Class<? extends SillyPropertyHandle> propertyHandleClazz) {
        this.propertyHandleClazz = propertyHandleClazz;
    }

    @Override
    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    @Override
    public Class<? extends SillyReadService> getDefaultReadServiceClazz() {
        return defaultReadServiceClazz;
    }

    public void setDefaultReadServiceClazz(Class<? extends SillyReadService> defaultReadServiceClazz) {
        this.defaultReadServiceClazz = defaultReadServiceClazz;
    }

    @Override
    public Class<? extends SillyWriteService> getDefaultWriteServiceClazz() {
        return defaultWriteServiceClazz;
    }

    public void setDefaultWriteServiceClazz(Class<? extends SillyWriteService> defaultWriteServiceClazz) {
        this.defaultWriteServiceClazz = defaultWriteServiceClazz;
    }
}
