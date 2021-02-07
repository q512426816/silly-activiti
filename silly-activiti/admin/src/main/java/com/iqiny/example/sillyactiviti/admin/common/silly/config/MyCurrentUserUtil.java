package com.iqiny.example.sillyactiviti.admin.common.silly.config;

import com.iqiny.example.sillyactiviti.admin.common.utils.SecurityUtils;
import com.iqiny.silly.common.util.CurrentUserUtil;
import org.springframework.stereotype.Component;

@Component
public class MyCurrentUserUtil implements CurrentUserUtil {

    @Override
    public String currentUserId() {
        return SecurityUtils.getUserId();
    }
    
}
