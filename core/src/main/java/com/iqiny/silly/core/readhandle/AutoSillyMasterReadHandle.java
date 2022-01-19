/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.readhandle;

import java.util.Map;

public class AutoSillyMasterReadHandle implements SillyMasterReadHandle{

    public static final String NAME = "autoMasterReadHandle";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void handle(Map<String, Object> record) {

    }
}
