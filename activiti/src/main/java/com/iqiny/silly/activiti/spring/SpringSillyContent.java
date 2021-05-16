/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti.spring;

import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.core.base.SillyInitializable;
import com.iqiny.silly.core.config.SillyConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 傻瓜 Spring 相关操作
 */
public class SpringSillyContent implements ApplicationContextAware, InitializingBean {

    private final static Log log = LogFactory.getLog(SpringSillyContent.class);

    protected static ApplicationContext applicationContext;

    /**
     * 获取Bean 同时会在容器中寻找相应Bean 注册到Spring环境中
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Set<T> getBeanSet(Class<T> clazz) {
        Set<T> set = new LinkedHashSet<>();
        final Set<Map.Entry<String, T>> entries = applicationContext.getBeansOfType(clazz).entrySet();
        for (Map.Entry<String, T> entry : entries) {
            set.add(entry.getValue());
        }
        return set;
    }

    public static Set<SillyInitializable> getBeanSet(Class<SillyInitializable> clazz, String category) {
        Set<SillyInitializable> set = getBeanSet(clazz);
        Set<SillyInitializable> returnSet = new LinkedHashSet<>();
        for (SillyInitializable sillyInit : set) {
            if (sillyInit.usedCategory().equals(category)) {
                returnSet.add(sillyInit);
            }
        }
        return returnSet;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static SillyInitializable getBean(Class<SillyInitializable> clazz, String category) {
        final Set<SillyInitializable> beanSet = getBeanSet(clazz, category);
        SillyAssert.isTrue((beanSet.size() != 1), clazz.getName() + " 对应的种类 " + category + " 期望找到 1 个匹配项，找到了 " + beanSet.size() + "个");
        return beanSet.iterator().next();
    }


    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringSillyContent.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initSillyConfig();
        initSillyInitializable();
    }

    protected void initSillyConfig() {
        // 开始初始化配置
        final Set<SillyConfig> beanSet = getBeanSet(SillyConfig.class);
        for (SillyConfig sillyConfig : beanSet) {
            // SillyConfig初始化！
            sillyConfig.init();
            log.info("SillyConfig:" + sillyConfig.getClass().getName() + " 初始化完成");
        }
    }

    protected void initSillyInitializable() {
        // 开始初始化配置
        final Set<SillyInitializable> beanSet = getBeanSet(SillyInitializable.class);
        for (SillyInitializable sillyInitializable : beanSet) {
            // 不对sillyConfig重新初始化！
            if (!(sillyInitializable instanceof SillyConfig)) {
                sillyInitializable.init();
                if (log.isDebugEnabled()) {
                    log.debug("SillyInitializable:" + sillyInitializable.getClass().getName() + " 初始化完成");
                }
            }
        }
    }

}
