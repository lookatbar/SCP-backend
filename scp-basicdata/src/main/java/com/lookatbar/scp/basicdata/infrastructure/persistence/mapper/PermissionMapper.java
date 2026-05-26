package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.PermissionPO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PermissionMapper extends BaseMapper<PermissionPO> {

    @Select("SELECT * FROM rbac_permission WHERE code = #{code}")
    Optional<PermissionPO> findByCode(@Param("code") String code);

    @Select("SELECT * FROM rbac_permission WHERE status = #{status}")
    List<PermissionPO> findByStatus(@Param("status") Integer status);

    @Select("SELECT * FROM rbac_permission WHERE type = #{type}")
    List<PermissionPO> findByType(@Param("type") Integer type);

    @Select("SELECT * FROM rbac_permission WHERE parent_id = #{parentId}")
    List<PermissionPO> findByParentId(@Param("parentId") Long parentId);

    @Select("SELECT p.* FROM rbac_permission p " +
            "JOIN rbac_role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    List<PermissionPO> findByRoleId(@Param("roleId") Long roleId);

    @Select("SELECT p.* FROM rbac_permission p " +
            "JOIN rbac_role_permission rp ON p.id = rp.permission_id " +
            "JOIN rbac_user_role ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} " +
            "UNION " +
            "SELECT p.* FROM rbac_permission p " +
            "JOIN rbac_role_permission rp ON p.id = rp.permission_id " +
            "JOIN rbac_role_hierarchy rh ON rp.role_id = rh.parent_role_id " +
            "JOIN rbac_user_role ur ON rh.child_role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<PermissionPO> findByUserId(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM rbac_permission WHERE code = #{code}")
    int countByCode(@Param("code") String code);
}