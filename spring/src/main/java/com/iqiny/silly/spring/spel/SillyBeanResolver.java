/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.spring.spel;

import com.iqiny.silly.core.base.SillyContext;
import com.iqiny.silly.spring.SpringSillyContext;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;

public class SillyBeanResolver implements BeanResolver {

    private final SillyContext sillyContext;

    public SillyBeanResolver(SillyContext sillyContext){
        this.sillyContext = sillyContext;
    }

    @Override
    public Object resolve(EvaluationContext context, String beanName) throws AccessException {
        return sillyContext.getBean(beanName);
    }
}
