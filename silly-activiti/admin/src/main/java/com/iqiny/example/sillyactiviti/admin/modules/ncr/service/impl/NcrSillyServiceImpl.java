package com.iqiny.example.sillyactiviti.admin.modules.ncr.service.impl;

import com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.service.MyWriteSillyService;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrMaster;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrNode;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrVariable;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.service.NcrSillyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class NcrSillyServiceImpl extends MyWriteSillyService<NcrMaster, NcrNode, NcrVariable> implements NcrSillyService {

    @Override
    public void submit(NcrMaster master, NcrNode node) {
        super.submit(master, node);
    }

    @Override
    public String usedCategory() {
        return "NCR";
    }
}
