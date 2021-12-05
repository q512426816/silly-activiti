/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti.spring;

import com.iqiny.silly.core.base.SillyInitializable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyConfigContent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 傻瓜 Spring 相关操作
 */
public class SpringSillyContent implements ApplicationContextAware, InitializingBean {

    private final static Log log = LogFactory.getLog(SpringSillyContent.class);

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private final static String SILLY_READ_SERVICE_PREFIX = "SillyReadService";
    private final static String SILLY_WRITE_SERVICE_PREFIX = "SillyWriteService";


    public static String getSillyReadServiceBeanName(String category) {
        return SILLY_READ_SERVICE_PREFIX + category;
    }

    public static String getSillyWriteServiceBeanName(String category) {
        return SILLY_WRITE_SERVICE_PREFIX + category;
    }

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

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T registerBean(String beanName, Class<T> clazz, Consumer<BeanDefinitionBuilder> consumer) {
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
        if (applicationContext.containsBean(beanName)) {
            // 若有相同名称的移出此Bean
            beanFactory.removeBeanDefinition(beanName);
            log.info("移出 BeanDefinition: " + beanName);
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        if (consumer != null) {
            consumer.accept(beanDefinitionBuilder);
        }

        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
        log.info("动态注册Bean 成功：" + beanName);
        return applicationContext.getBean(beanName, clazz);
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
        final Set<SillyConfigContent> beanSet = getBeanSet(SillyConfigContent.class);
        for (SillyConfigContent sillyCategoryConfig : beanSet) {
            // SillyConfig初始化！
            sillyCategoryConfig.init();
            log.info("SillyConfig:" + sillyCategoryConfig.getClass().getName() + " 初始化完成");
        }
    }

    protected void initSillyInitializable() {
        // 开始初始化配置
        final Set<SillyInitializable> beanSet = getBeanSet(SillyInitializable.class);
        for (SillyInitializable sillyInitializable : beanSet) {
            // 不对sillyConfig重新初始化！
            if (!(sillyInitializable instanceof SillyCategoryConfig)) {
                sillyInitializable.init();
                if (log.isDebugEnabled()) {
                    log.debug("SillyInitializable:" + sillyInitializable.getClass().getName() + " 初始化完成");
                }
            }
        }
    }

}
