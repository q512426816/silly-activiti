package com.iqiny.silly.core.resume;

import java.util.List;

/**
 * 流程履历服务接口
 */
public interface SillyResumeService {

    String makeResumeHandleInfo(String nextUserIds, String taskName, String content);

    void insert(SillyResume process);

    List<? extends SillyResume> selectList(String masterId, String businessType);

}
