/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.spring.spel;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyContext;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.Objects;

/**
 * 集成 SPEL 表达式配置
 */
public class SillySpelPropertyHandle implements SillyPropertyHandle {

    public static final String EL_START = "${";
    public static final String EL_END = "}";

    private StandardEvaluationContext context;
    private SillyContext sillyContext;
    private Map<String, Object> values;
    private Object root;

    public StandardEvaluationContext getContext() {
        if (context == null) {
            context = new StandardEvaluationContext(root);
            context.setBeanResolver(new SillyBeanResolver(sillyContext));
            context.setVariables(getValues());
        }
        return context;
    }

    public SillySpelPropertyHandle setContext(StandardEvaluationContext context) {
        this.context = context;
        return this;
    }

    @Override
    public Map<String, Object> getValues() {
        return values;
    }

    public SillySpelPropertyHandle setValues(Map<String, Object> values) {
        this.values = values;
        return this;
    }

    public boolean isSillySpel(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }

        int start = str.indexOf(EL_START);
        int end = str.lastIndexOf(EL_END);
        return start < end;
    }

    public Object getSpelValue(String expression) {
        int start = expression.indexOf(EL_START);
        int end = expression.lastIndexOf(EL_END);
        String substring = expression.substring(start + EL_START.length(), end);
        return doGetSpelValue(substring);
    }

    protected Object doGetSpelValue(String expression) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expression);
        try {
            return exp.getValue(getContext());
        } catch (Exception e) {
            if (e.getCause() != null) {
                throw new SillyException(e.getCause().getMessage(), e);
            } else {
                throw new SillyException("SPEL解析异常 " + e.getMessage(), e);
            }
        }
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public void setRoot(Object root) {
        this.root = root;
    }

    @Override
    public Object getValue(String expression) {
        return isSillySpel(expression) ? getSpelValue(expression) : expression;
    }

    @Override
    public boolean getBooleanValue(String expression) {
        if (StringUtils.isEmpty(expression)) {
            return false;
        }

        Object value = getValue(expression);
        if (value instanceof Boolean) {
            return (boolean) value;
        }

        if (Objects.equals(value, SillyConstant.YesOrNo.YES)) {
            return true;
        }
        return Boolean.parseBoolean(expression);
    }

    @Override
    public void setValues(Object values) {
        SillyAssert.notNull(values, "配置处理器上下文对象不可为null");
        SillyAssert.isTrue(values instanceof Map, "配置处理器类型不支持" + values.getClass());
        setValues((Map<String, Object>) values);
    }

    @Override
    public void setSillyContext(SillyContext sillyContext) {
        this.sillyContext = sillyContext;
    }

    @Override
    public void updateValue(String key, Object value) {
        values.put(key, value);
        getContext().setVariable(key, value);
    }

}
