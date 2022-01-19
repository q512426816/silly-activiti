/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.handle;

import com.iqiny.silly.core.base.SillyCategory;

public abstract class BaseSillyMasterConvertorReadHandle implements SillyMasterConvertorReadHandle, SillyCategory {

    @Override
    public String name() {
        return usedCategory() + getClass().getSimpleName();
    }

}
