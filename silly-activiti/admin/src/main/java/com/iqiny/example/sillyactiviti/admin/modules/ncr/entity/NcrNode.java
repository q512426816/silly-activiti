/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.ncr.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.entity.MySillyNode;

@TableName("silly_ncr_node")
public class NcrNode extends MySillyNode<NcrNode, NcrVariable> {
    @Override
    public String category() {
        return NcrMaster.CATEGORY;
    }
}
