/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iqiny.example.sillyactiviti.admin.common.annotation.DataFilter;
import com.iqiny.example.sillyactiviti.admin.modules.sys.dao.SysDeptDao;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysDeptEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysDeptService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "t1")
    public List<SysDeptEntity> queryList(Map<String, Object> params) {
        return baseMapper.queryList(params);
    }

    @Override
    public List<String> queryDetpIdList(String parentId) {
        return baseMapper.queryDetpIdList(parentId);
    }

    @Override
    public List<String> getSubDeptIdList(String deptId) {
        //部门及子部门ID列表
        List<String> deptIdList = new ArrayList<>();

        //获取子部门ID
        List<String> subIdList = queryDetpIdList(deptId);
        getDeptTreeList(subIdList, deptIdList);

        return deptIdList;
    }

    /**
     * 递归
     */
    private void getDeptTreeList(List<String> subIdList, List<String> deptIdList) {
        for (String deptId : subIdList) {
            List<String> list = queryDetpIdList(deptId);
            if (list.size() > 0) {
                getDeptTreeList(list, deptIdList);
            }

            deptIdList.add(deptId);
        }
    }
}
