/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.spring.scan;

import com.iqiny.silly.common.util.SillyReflectUtil;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.resume.SillyResume;

/**
 * 通过扫描包 生产的实体工厂
 *
 * @param <M>
 * @param <N>
 * @param <V>
 * @param <R>
 */
public class ScanSillyFactory<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable, R extends SillyResume>
        implements SillyFactory<M, N, V, R> {

    private final String category;
    private Class<M> masterClazz;
    private Class<N> nodeClazz;
    private Class<V> variableClazz;
    private Class<R> resumeClazz;


    public ScanSillyFactory(String category) {
        this.category = category;
    }

    @Override
    public String usedCategory() {
        return category;
    }

    @Override
    public M newMaster() {
        return SillyReflectUtil.newInstance(masterClazz);
    }

    @Override
    public Class<M> masterClass() {
        return masterClazz;
    }

    @Override
    public N newNode() {
        return SillyReflectUtil.newInstance(nodeClazz);
    }

    @Override
    public Class<N> nodeClass() {
        return nodeClazz;
    }

    @Override
    public V newVariable() {
        return SillyReflectUtil.newInstance(variableClazz);
    }

    @Override
    public Class<V> variableClass() {
        return variableClazz;
    }

    @Override
    public R newResume() {
        return SillyReflectUtil.newInstance(resumeClazz);
    }

    @Override
    public Class<R> resumeClass() {
        return resumeClazz;
    }

    public void setMasterClazz(Class<M> masterClazz) {
        this.masterClazz = masterClazz;
    }

    public void setNodeClazz(Class<N> nodeClazz) {
        this.nodeClazz = nodeClazz;
    }

    public void setVariableClazz(Class<V> variableClazz) {
        this.variableClazz = variableClazz;
    }

    public void setResumeClazz(Class<R> resumeClazz) {
        this.resumeClazz = resumeClazz;
    }

    @SuppressWarnings("all")
    public void setEntityClazz(Class<?> clazz) {
        if (SillyMaster.class.isAssignableFrom(clazz)) {
            masterClazz = (Class<M>) clazz;
        } else if (SillyNode.class.isAssignableFrom(clazz)) {
            nodeClazz = (Class<N>) clazz;
        } else if (SillyVariable.class.isAssignableFrom(clazz)) {
            variableClazz = (Class<V>) clazz;
        } else if (SillyResume.class.isAssignableFrom(clazz)) {
            resumeClazz = (Class<R>) clazz;
        }
    }

    @SuppressWarnings("all")
    public void setDefaultClazz(ScanSillyFactory supportAll) {
        if (masterClazz == null) {
            masterClazz = supportAll.masterClazz;
        }
        if (nodeClazz == null) {
            nodeClazz = supportAll.nodeClazz;
        }
        if (variableClazz == null) {
            variableClazz = supportAll.variableClazz;
        }
        if (resumeClazz == null) {
            resumeClazz = supportAll.resumeClazz;
        }
    }
}
