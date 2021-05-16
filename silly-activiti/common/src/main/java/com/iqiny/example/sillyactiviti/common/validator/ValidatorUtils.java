/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.common.validator;


import com.iqiny.example.sillyactiviti.common.exception.MyException;
import com.iqiny.example.sillyactiviti.common.validator.group.AddGroup;
import com.iqiny.example.sillyactiviti.common.validator.group.UpdateGroup;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * hibernate-validator校验工具类
 *
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 *
 *
 */
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws MyException  校验不通过，则报RRException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws MyException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
        	ConstraintViolation<Object> constraint = constraintViolations.iterator().next();
            throw new MyException(constraint.getMessage());
        }
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @throws MyException  校验不通过，则报RRException异常
     */
    public static void validateAddEntity(Object object)
            throws MyException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, AddGroup.class);
        if (!constraintViolations.isEmpty()) {
        	ConstraintViolation<Object> constraint = constraintViolations.iterator().next();
            throw new MyException(constraint.getMessage());
        }
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @throws MyException  校验不通过，则报RRException异常
     */
    public static void validateUpdateEntity(Object object)
            throws MyException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, UpdateGroup.class);
        if (!constraintViolations.isEmpty()) {
        	ConstraintViolation<Object> constraint = constraintViolations.iterator().next();
            throw new MyException(constraint.getMessage());
        }
    }
}
