package com.iqiny.example.sillyactiviti.admin.modules.ncr.service.impl;

import com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.service.MySillyService;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrMaster;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrNode;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrVariable;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.service.NcrSillyService;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.silly.NcrSillyFactory;
import com.iqiny.silly.core.base.SillyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class NcrSillyServiceImpl extends MySillyService<NcrMaster, NcrNode, NcrVariable> implements NcrSillyService {

    @Override
    protected String processResumeBusinessType() {
        return null;
    }

    @Override
    protected SillyFactory<NcrMaster, NcrNode, NcrVariable> createSillyFactory() {
        return NcrSillyFactory.get();
    }

    @Override
    public void submit(NcrMaster master, NcrNode node) {
        super.submit(master, node);
    }
}
