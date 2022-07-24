package com.sqmusicplus.config;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Classname EnumValueValidator
 * @Description 枚举校验注解处理类
 * @Version 1.0.0
 * @Date 2022/6/2 17:05
 * @Created by SQ
 */

public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {
    private String[] strValues;
    private int[] intValues;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        strValues = constraintAnnotation.strValues();
        intValues = constraintAnnotation.intValues();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof String) {
            for (String s : strValues) {
                if (s.equals(value)) {
                    return true;
                }
            }
        } else if (value instanceof Integer) {
            for (int s : intValues) {
                if (s == ((Integer) value).intValue()) {
                    return true;
                }
            }
        }
        return false;
    }

}
