package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.RolePermissionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermissionPO> {

    @Insert("INSERT INTO rbac_role_permission (role_id, permission_id) VALUES (#{roleId}, #{permissionId})")
    void insertRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    @Delete("DELETE FROM rbac_role_permission WHERE role_id = #{roleId} AND permission_id = #{permissionId}")
    void deleteRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    @Delete("DELETE FROM rbac_role_permission WHERE role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") Long roleId);

    @Delete("DELETE FROM rbac_role_permission WHERE permission_id = #{permissionId}")
    void deleteByPermissionId(@Param("permissionId") Long permissionId);
}