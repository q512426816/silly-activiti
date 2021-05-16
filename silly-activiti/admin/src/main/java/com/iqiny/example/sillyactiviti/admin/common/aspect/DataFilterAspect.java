/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.aspect;

import com.iqiny.example.sillyactiviti.admin.common.annotation.DataFilter;
import com.iqiny.example.sillyactiviti.admin.common.utils.Constant;
import com.iqiny.example.sillyactiviti.admin.common.utils.SecurityUtils;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysUserEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysDeptService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysRoleDeptService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysUserRoleService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysUserService;
import com.iqiny.example.sillyactiviti.common.exception.MyException;
import com.iqiny.example.sillyactiviti.common.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据过滤，切面处理类
 *
 *
 */
@Aspect
@Component
public class DataFilterAspect {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;
    @Autowired
    private SysUserService sysUserService;

    @Pointcut("@annotation(com.iqiny.example.sillyactiviti.admin.common.annotation.DataFilter)")
    public void dataFilterCut() {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) throws Throwable {
        Object params = point.getArgs()[0];
        if (params instanceof Map) {
            String username = SecurityUtils.getUsername();
            final SysUserEntity userEntity = sysUserService.getByUsername(username);
            //如果不是超级管理员，则进行数据过滤
            if (!Constant.SUPER_ADMIN.equals(userEntity.getId())) {
                Map map = (Map) params;
                map.put(Constant.SQL_FILTER, getSQLFilter(userEntity, point));
            }

            return;
        }

        throw new MyException("数据权限接口，只能是Map类型参数，且不能为NULL");
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSQLFilter(SysUserEntity user, JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);
        //获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if (StringUtils.isNotBlank(tableAlias)) {
            tableAlias += ".";
        }

        //部门ID列表
        Set<String> deptIdList = new HashSet<>();

        //用户角色对应的部门ID列表
        List<String> roleIdList = sysUserRoleService.queryRoleIdList(user.getId());
        if (roleIdList.size() > 0) {
            List<String> userDeptIdList = sysRoleDeptService.queryDeptIdList(roleIdList.toArray(new String[0]));
            deptIdList.addAll(userDeptIdList);
        }

        //用户子部门ID列表
        if (dataFilter.subDept()) {
            List<String> subDeptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
            deptIdList.addAll(subDeptIdList);
        }

        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");

        if (deptIdList.size() > 0) {
            sqlFilter.append(tableAlias).append(dataFilter.deptId()).append(" in(").append(StringUtils.join(deptIdList, ",")).append(")");
        }

        //没有本部门数据权限，也能查询本人数据
        if (dataFilter.user()) {
            if (deptIdList.size() > 0) {
                sqlFilter.append(" or ");
            }
            sqlFilter.append(tableAlias).append(dataFilter.userId()).append("=").append(user.getId());
        }

        sqlFilter.append(")");

        if ("()".equals(sqlFilter.toString().trim())) {
            return null;
        }

        return sqlFilter.toString();
    }
}
