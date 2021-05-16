/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.sys.controller;


import com.iqiny.example.sillyactiviti.common.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录相关
 *
 *
 */
@Controller
public class SysLoginController {
    //@Autowired
    // private Producer producer;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenProvider tokenProvider;

    @RequestMapping("captcha.jpg")
    public void captcha(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成文字验证码
        //String text = producer.createText();
        //生成图片验证码
        //BufferedImage image = producer.createImage(text);
        //保存到shiro session
        //ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        //ServletOutputStream out = response.getOutputStream();
        //ImageIO.write(image, "jpg", out);
    }

    /* */

    /**
     * 登录
     *//*
    @ResponseBody
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    public R login(String username, String password, String captcha) {
        try {
           // String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
            //if (!captcha.equalsIgnoreCase(kaptcha)) {
                //return R.error("验证码不正确");
            //}

            Subject subject = ShiroUtils.getAuthentication();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
        } catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return R.error("账号或密码不正确");
        } catch (LockedAccountException e) {
            return R.error("账号已被锁定,请联系管理员");
        } catch (AuthenticationException e) {
            return R.error("账户验证失败");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }

        return R.ok();
    }*/

    /**
     * 退出
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        //ShiroUtils.logout();
        return "redirect:login.html";
    }

}
