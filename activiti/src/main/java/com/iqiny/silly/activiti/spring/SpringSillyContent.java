package com.iqiny.silly.activiti.spring;

import com.iqiny.silly.activiti.ActivitiSillyConfig;
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

public class SpringSillyContent implements ApplicationContextAware, InitializingBean {

    private final static Log log = LogFactory.getLog(SpringSillyContent.class);

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
        System.out.println("===============================================");
        System.out.println("");
        System.out.println("欢迎使用 Silly【1.0.2-RELEASE】流程数据服务框架");
        System.out.println("");
        System.out.println("===============================================");

        this.applicationContext = applicationContext;
        // 开始初始化配置
        final Set<ActivitiSillyConfig> beanSet = getBeanSet(ActivitiSillyConfig.class);
        for (ActivitiSillyConfig sillyConfig : beanSet) {
            // SillyConfig初始化！
            sillyConfig.setSpringSillyContent(this);
            sillyConfig.init();
            if (log.isDebugEnabled()) {
                log.debug("SillyConfig:" + sillyConfig.getClass().getName() + " 初始化完成");
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
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
