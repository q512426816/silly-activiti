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
import com.iqiny.silly.core.base.SillyOrdered;
import com.iqiny.silly.core.common.SillyCoreUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
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
    private final static String SILLY_TASK_CATEGORY_GROUP_PREFIX = "SillyTaskCategoryGroup";


    public static String getSillyReadServiceBeanName(String category) {
        return SILLY_READ_SERVICE_PREFIX + category;
    }

    public static String getSillyWriteServiceBeanName(String category) {
        return SILLY_WRITE_SERVICE_PREFIX + category;
    }

    public static String getSillyEngineServiceBeanName(String category) {
        return SILLY_ENGINE_SERVICE_PREFIX + category;
    }

    public static String getSillyTaskCategoryGroupBeanName(String category, String variableName, String groupName) {
        return SILLY_TASK_CATEGORY_GROUP_PREFIX + category + variableName + groupName;
    }

    /**
     * 获取Bean 同时会在容器中寻找相应Bean 注册到Spring环境中
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> getBeanList(String category, Class<T> clazz) {
        List<T> beanList = new ArrayList<>();
        try {
            final Set<Map.Entry<String, T>> entries = applicationContext.getBeansOfType(clazz).entrySet();
            for (Map.Entry<String, T> entry : entries) {
                T value = entry.getValue();
                boolean available = SillyCoreUtil.available(category, value);
                if (available) {
                    beanList.add(value);
                }
            }
        } catch (BeansException ignore) {
        }

        return SillyCoreUtil.orderCollection(beanList);
    }


    @Override
    public <T> T getBean(String category, String beanName) {
        try {
            Object bean = applicationContext.getBean(beanName);
            return (T) SillyCoreUtil.availableOrNull(category, bean);
        } catch (BeansException ignore) {
        }
        return null;
    }

    @Override
    public <T> T getBean(String category, Class<T> clazz) {
        List<T> list = getBeanList(category, clazz);
        return SillyCoreUtil.availableOne(category, list);
    }


    @Override
    public <T extends SillyOrdered> T getNextBean(SillyOrdered order, String category, Class<T> clazz) {
        List<T> list = getBeanList(category, clazz);
        if (!list.isEmpty() && order == null) {
            return list.get(0);
        }

        for (T t : list) {
            if (t.order() == order.order()) {
                if (t.hashCode() > order.hashCode()) {
                    return t;
                }
            } else if (t.order() > order.order()) {
                return t;
            }
        }
        return null;
    }

    @Override
    public <T> T registerBean(String category, String beanName, Class<T> clazz, Consumer<Object> consumer) {
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
        if (applicationContext.containsBean(beanName)) {
            // 若有相同名称的移出此Bean
            beanFactory.removeBeanDefinition(beanName);
            if (log.isDebugEnabled()) {
                log.debug("移出 BeanDefinition: " + beanName);
            }
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        if (consumer != null) {
            consumer.accept(beanDefinitionBuilder);
        }

        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
        if (log.isDebugEnabled()) {
            log.debug("动态注册Bean 成功：" + beanName);
        }
        return applicationContext.getBean(beanName, clazz);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringSillyContext.applicationContext = applicationContext;
    }

}
