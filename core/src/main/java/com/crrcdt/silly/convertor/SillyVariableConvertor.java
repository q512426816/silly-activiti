package com.crrcdt.silly.convertor;

import java.util.Map;

public interface SillyVariableConvertor<T> {

    T convert(Map<String, Object> map, String key, String value);

}
