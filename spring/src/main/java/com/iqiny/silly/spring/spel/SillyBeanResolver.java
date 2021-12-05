/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.spring.spel;

import com.iqiny.silly.spring.SpringSillyContent;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;

public class SillyBeanResolver implements BeanResolver {

    private static final SillyBeanResolver RESOLVER = new SillyBeanResolver();

    public static BeanResolver get() {
        return RESOLVER;
    }

    @Override
    public Object resolve(EvaluationContext context, String beanName) throws AccessException {
        return SpringSillyContent.getBean(beanName);
    }
}
