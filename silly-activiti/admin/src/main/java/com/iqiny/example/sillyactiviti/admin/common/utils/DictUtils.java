package com.iqiny.example.sillyactiviti.admin.common.utils;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysDictEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysDictService;
import com.iqiny.example.sillyactiviti.common.utils.SpringContextUtils;
import com.iqiny.example.sillyactiviti.common.utils.StringUtils;

public class DictUtils {

    public class DictType {
        public static final String YES_NO = "YES_NO";
        public static final String GENDER = "GENDER";
        public static final String PERSON_TYPE = "PERSON_TYPE";
        public static final String LOG_TYPE = "LOG_TYPE";
        public static final String EDU_CHANNEL = "EDU_CHANNEL";
        public static final String PAY_STATUS = "PAY_STATUS";
        public static final String GENERALIZE_GRADE = "GENERALIZE_GRADE";

    }

    private static SysDictService sysDictService = SpringContextUtils.getBean(SysDictService.class);

    public static String getName(String code, String type) {
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(type)) {
            return "";
        }
        SysDictEntity entity = new SysDictEntity();
        entity.setCode(code);
        entity.setType(type);
        QueryWrapper<SysDictEntity> qw = new QueryWrapper<>();
        qw.setEntity(entity);
        final SysDictEntity one = sysDictService.getOne(qw);
        return one == null ? null : one.getValue();
    }

}
