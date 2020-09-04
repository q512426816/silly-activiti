package com.crrcdt.silly;

import com.crrcdt.silly.base.SillyNodeBean;
import com.crrcdt.silly.base.core.SillyMaster;
import com.crrcdt.silly.base.core.SillyNode;
import com.crrcdt.silly.base.core.SillyVariable;
import com.crrcdt.silly.resume.SillyResume;

/**
 * 抽象工厂，定义统一系列业务对象
 *
 * @param <M> 主表
 * @param <N> 节点表
 * @param <V> 变量表
 */
public abstract class AbstractSillyFactory<M extends SillyMaster, N extends SillyNode, V extends SillyVariable> {

    public abstract M newMaster();

    public abstract N newNode();

    public abstract V newVariable();

    public SillyResume newResume() {
        throw new RuntimeException("请重写newResume方法，创建流程履历对象！");
    }

    public abstract SillyNodeBean<N, V> newSillyNodeBean();
}
