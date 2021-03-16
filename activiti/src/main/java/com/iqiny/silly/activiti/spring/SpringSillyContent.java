package com.iqiny.silly.activiti.spring;

import com.iqiny.silly.activiti.ActivitiSillyConfig;
import com.iqiny.silly.core.base.SillyInitializable;
import com.iqiny.silly.core.config.SillyConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SpringSillyContent implements ApplicationContextAware, InitializingBean {

    protected ApplicationContext applicationContext;

    public <T> Set<T> getBeanSet(Class<T> clazz) {
        Set<T> set = new LinkedHashSet<>();
        final Set<Map.Entry<String, T>> entries = applicationContext.getBeansOfType(clazz).entrySet();
        for (Map.Entry<String, T> entry : entries) {
            set.add(entry.getValue());
        }
        return set;
    }

    public <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        // 开始初始化配置
        final Set<ActivitiSillyConfig> beanSet = getBeanSet(ActivitiSillyConfig.class);
        for (ActivitiSillyConfig sillyConfig : beanSet) {
            // SillyConfig初始化！
            sillyConfig.setSpringSillyContent(this);
            sillyConfig.init();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final Set<SillyInitializable> beanSet = getBeanSet(SillyInitializable.class);
        for (SillyInitializable sillyInitializable : beanSet) {
            // 不对sillyConfig重新初始化！
            if (!(sillyInitializable instanceof SillyConfig)) {
                sillyInitializable.init();
            }
        }
    }
}
