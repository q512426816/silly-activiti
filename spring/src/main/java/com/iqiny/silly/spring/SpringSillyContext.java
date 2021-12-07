/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.spring;

import com.iqiny.silly.core.base.SillyContext;
import com.iqiny.silly.core.base.SillyInitializable;
import com.iqiny.silly.core.common.SillyCoreUtil;
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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 傻瓜 Spring 相关操作
 */
public class SpringSillyContext implements SillyContext, ApplicationContextAware {

    private final static Log log = LogFactory.getLog(SpringSillyContext.class);

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private final static String SILLY_READ_SERVICE_PREFIX = "SillyReadService";
    private final static String SILLY_WRITE_SERVICE_PREFIX = "SillyWriteService";
    private final static String SILLY_ENGINE_SERVICE_PREFIX = "SillyEngineService";


    public static String getSillyReadServiceBeanName(String category) {
        return SILLY_READ_SERVICE_PREFIX + category;
    }

    public static String getSillyWriteServiceBeanName(String category) {
        return SILLY_WRITE_SERVICE_PREFIX + category;
    }

    public static String getSillyEngineServiceBeanName(String category) {
        return SILLY_ENGINE_SERVICE_PREFIX + category;
    }

    /**
     * 获取Bean 同时会在容器中寻找相应Bean 注册到Spring环境中
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @Override
    public <T> Set<T> getBeanSet(String category, Class<T> clazz) {
        Set<T> set = new LinkedHashSet<>();
        final Set<Map.Entry<String, T>> entries = applicationContext.getBeansOfType(clazz).entrySet();
        for (Map.Entry<String, T> entry : entries) {
            T value = entry.getValue();
            boolean available = SillyCoreUtil.available(category, value);
            if (available) {
                set.add(value);
            }
        }
        return set;
    }


    @Override
    public <T> T getBean(String category, String beanName) {
        Object bean = applicationContext.getBean(beanName);
        return (T) SillyCoreUtil.availableOrNull(category, bean);
    }

    @Override
    public <T> T getBean(String category, Class<T> clazz) {
        Set<T> sets = getBeanSet(category, clazz);
        return SillyCoreUtil.availableOne(category, new ArrayList<>(sets));
    }

    @Override
    public <T> T registerBean(String category, String beanName, Class<T> clazz, Consumer<Object> consumer) {
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
        SpringSillyContext.applicationContext = applicationContext;
    }

}
