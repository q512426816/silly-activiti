/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.common.security.jwt;

import com.iqiny.example.sillyactiviti.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 认证通过之后的处理器， 生产 Token 返回客户端
 */
@Component
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        //拿到登录用户信息
        User user = (User) authentication.getPrincipal();
        //生成token
        String jwtToken = tokenProvider.createToken(user.getUsername(), new Date());
        R result = R.ok("登录成功").put(tokenProvider.tokenName(), jwtToken);

        httpServletResponse.getWriter().write(result.toJson());
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json");
    }
}