package com.iqiny.example.sillyactiviti.admin.modules.ncr.silly;

import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrMaster;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrNode;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrVariable;
import com.iqiny.silly.core.base.SillyTaskData;
import lombok.Data;

import java.util.List;

@Data
public class NcrSillySaveData implements SillyTaskData<NcrNode,NcrVariable> {

    private NcrMaster master;

    private NcrNode node;

    private List<NcrVariable> variableList;

}
