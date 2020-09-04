package com.crrcdt.silly.resume;

import java.util.List;

public interface SillyResumeService {

    String makeResumeHandleInfo(String nextUserIds, String taskName, String content);

    void insert(SillyResume process);
    
    List<? extends SillyResume> selectList(String masterId, String businessType);

}
