/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.resume;

import com.iqiny.silly.core.base.SillyInitializable;

import java.util.List;

/**
 * 流程履历服务接口
 */
public interface SillyResumeService<R extends SillyResume> extends SillyInitializable {

    /**
     * 生成处置履历内容
     *
     * @param handleType
     * @param nextUserIds
     * @param taskName
     * @param content
     * @return
     */
    String makeResumeHandleInfo(String handleType, String nextUserIds, String taskName, String content);

    void insert(R process);

    List<R> selectList(String masterId, String businessType);

}
