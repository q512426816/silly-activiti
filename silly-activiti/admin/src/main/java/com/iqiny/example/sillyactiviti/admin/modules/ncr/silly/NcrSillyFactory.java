package com.iqiny.example.sillyactiviti.admin.modules.ncr.silly;

import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrMaster;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrNode;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrVariable;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.SillyTaskData;
import com.iqiny.silly.core.resume.SillyResume;

public class NcrSillyFactory implements SillyFactory<NcrMaster, NcrNode, NcrVariable> {

    private static final NcrSillyFactory SILLY_FACTORY = new NcrSillyFactory();

    private NcrSillyFactory() {
    }

    public static NcrSillyFactory get() {
        return SILLY_FACTORY;
    }

    @Override
    public NcrMaster newMaster() {
        return new NcrMaster();
    }

    @Override
    public NcrNode newNode() {
        return new NcrNode();
    }

    @Override
    public NcrVariable newVariable() {
        return new NcrVariable();
    }

    @Override
    public SillyTaskData<NcrNode, NcrVariable> newSillyTaskData() {
        return null;
    }

    @Override
    public <T extends SillyResume> T newResume() {
        return null;
    }
}
