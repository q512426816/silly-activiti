package com.iqiny.example.sillyactiviti.admin.modules.ncr.service;

import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrMaster;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrNode;

public interface NcrSillyService {

    void submit(NcrMaster master, NcrNode node);
}
