package com.iqiny.example.sillyactiviti.admin.modules.ncr.silly;

import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrMaster;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrNode;
import lombok.Data;

@Data
public class NcrSillyDataMap {

    private NcrMaster master;

    private NcrNode node;
    
}
