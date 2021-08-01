package com.iqiny.silly.activiti.spring;

import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * 集成 SPEL 表达式配置
 */
public class SillySpelPropertyHandle implements SillyPropertyHandle {

    public static final String EL_START = "${";
    public static final String EL_END = "}";

    private EvaluationContext context;
    private Map<String, Object> values;

    public EvaluationContext getContext() {
        if (context == null) {
            context = new StandardEvaluationContext();
            ((StandardEvaluationContext) context).setVariables(getValues());
        }
        return context;
    }

    public SillySpelPropertyHandle setContext(EvaluationContext context) {
        this.context = context;
        return this;
    }

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
        String startString = expression.substring(0, start);
        String endString = expression.substring(end + EL_END.length());
        String substring = expression.substring(start + EL_START.length(), end);

        return startString + doGetSpelValue(substring) + endString;
    }

    protected String doGetSpelValue(String expression) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expression);
        return exp.getValue(getContext(), String.class);
    }

    @Override
    public Object getValue(String expression) {
        return isSillySpel(expression) ? getSpelValue(expression) : expression;
    }

    @Override
    public void setValues(Object values) {
        SillyAssert.notNull(values, "配置处理器上下文对象不可为null");
        SillyAssert.isTrue(values instanceof Map, "配置处理器类型不支持" + values.getClass());
        setValues((Map<String, Object>) values);
    }

}
