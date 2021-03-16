package com.iqiny.example.sillyactiviti.admin.modules.ncr.silly;

import com.iqiny.example.sillyactiviti.admin.common.silly.entity.MySillyResume;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrMaster;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrNode;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrVariable;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.SillyTaskData;
import com.iqiny.silly.core.resume.SillyResume;
import org.springframework.stereotype.Component;

@Component
public class NcrSillyFactory implements SillyFactory<NcrMaster, NcrNode, NcrVariable> {

    private NcrSillyFactory() {
    }

    @Override
    public String category() {
        return NcrMaster.CATEGORY;
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
        return new NcrSillySaveData();
    }

    @Override
    public SillyResume newResume() {
        return new MySillyResume();
    }
}
