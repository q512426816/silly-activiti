package com.iqiny.silly.core.resume;

import java.util.List;

/**
 * 流程履历服务接口
 */
public interface SillyResumeService<R extends SillyResume> {

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
