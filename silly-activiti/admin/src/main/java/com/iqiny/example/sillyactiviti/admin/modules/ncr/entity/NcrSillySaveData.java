/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.ncr.entity;

import com.iqiny.silly.core.base.SillyTaskData;
import lombok.Data;

import java.util.List;

@Data
public class NcrSillySaveData implements SillyTaskData<NcrNode, NcrVariable> {

    private NcrMaster master;

    private NcrNode node;

    private List<NcrVariable> variableList;

    @Override
    public String category() {
        return NcrMaster.CATEGORY;
    }
}
