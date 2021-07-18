/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.ncr.entity;

import lombok.Data;

import java.util.List;

@Data
public class NcrSillySaveData {

    private NcrMaster master;

    private NcrNode node;

    private List<NcrVariable> variableList;

    public String category() {
        return NcrMaster.CATEGORY;
    }
}
