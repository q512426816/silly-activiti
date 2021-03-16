package com.iqiny.silly.core.config;

import com.iqiny.silly.common.util.CurrentUserUtil;
import com.iqiny.silly.core.base.SillyInitializable;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;

import java.util.Map;

/**
 * 配置类
 */
public interface SillyConfig extends SillyInitializable {

    CurrentUserUtil getCurrentUserUtil();

    SillyEngineService getSillyEngineService();

    Map<String, SillyVariableConvertor> getSillyConvertorMap();

    SillyResumeService getSillyResumeService();

    SillyFactory getSillyFactory(String category);

}
