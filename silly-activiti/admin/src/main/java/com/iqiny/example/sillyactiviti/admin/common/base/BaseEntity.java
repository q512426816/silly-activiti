package com.iqiny.example.sillyactiviti.admin.common.base;

import java.util.Date;

/**
 * 基础实体字段
 *
 * @author qiny
 * @email qy-1994-2008@163.com
 * @date 2020-10-11 14:46:35
 */
public interface BaseEntity {
    
    String getId();

    void setId(String id);

    Integer getDelFlag();

    void setDelFlag(Integer delFlag);

    Date getCreateDate();

    void setCreateDate(Date createDate);

    String getCreateUserId();

    void setCreateUserId(String createUserId);

    Date getUpdateDate();

    void setUpdateDate(Date updateDate);

    String getUpdateUserId();

    void setUpdateUserId(String updateUserId);

    String getUpdateUserName();

    void setUpdateUserName(String updateUserName);

    String getCreateUserName();

    void setCreateUserName(String createUserName);

    void preInsert();

    void preUpdate();

}
