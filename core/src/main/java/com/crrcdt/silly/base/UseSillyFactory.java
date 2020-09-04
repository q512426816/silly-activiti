package com.crrcdt.silly.base;

import com.crrcdt.silly.AbstractSillyFactory;
import com.crrcdt.silly.base.core.SillyMaster;
import com.crrcdt.silly.base.core.SillyNode;
import com.crrcdt.silly.base.core.SillyVariable;

public interface UseSillyFactory<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> {
    
    AbstractSillyFactory<M, N, V> getSillyFactory();
    
}
