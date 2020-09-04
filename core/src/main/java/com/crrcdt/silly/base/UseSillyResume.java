package com.crrcdt.silly.base;

import com.crrcdt.silly.base.core.SillyMaster;
import com.crrcdt.silly.base.core.SillyNode;
import com.crrcdt.silly.base.core.SillyVariable;
import com.crrcdt.silly.resume.SillyResumeService;

public interface UseSillyResume<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> {

    SillyResumeService getSillyResumeService();
        
}
