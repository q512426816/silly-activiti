package com.iqiny.silly.core.config;

import com.iqiny.silly.common.util.CurrentUserUtil;
import com.iqiny.silly.core.base.Initializable;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;

import java.util.Map;

/**
 * 配置类
 */
public interface SillyConfig extends Initializable {

    CurrentUserUtil getCurrentUserUtil();

    void setCurrentUserUtil(CurrentUserUtil currentUserUtil);

    SillyEngineService getSillyEngineService();

    void setSillyEngineService(SillyEngineService sillyEngineService);

    Map<String, SillyVariableConvertor> getSillyConvertorMap();

    void setSillyConvertorMap(Map<String, SillyVariableConvertor> sillyConvertorMap);

    SillyResumeService getSillyResumeService();
    
    void setSillyResumeService(SillyResumeService sillyResumeService);
}
