package com.iqiny.example.sillyactiviti.admin.modules.ncr.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.entity.MySillyVariable;

@TableName("silly_ncr_variable")
public class NcrVariable extends MySillyVariable<NcrVariable> {

    @Override
    public String getBelong() {
        return null;
    }

    @Override
    public void setBelong(String belong) {

    }
}
