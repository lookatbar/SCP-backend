package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.RolePermissionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermissionPO> {

    @Insert("INSERT INTO basicdata_role_permission (role_id, permission_id) VALUES (#{roleId}, #{permissionId})")
    void insertRolePermission(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

    @Delete("DELETE FROM basicdata_role_permission WHERE role_id = #{roleId} AND permission_id = #{permissionId}")
    void deleteRolePermission(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

    @Delete("DELETE FROM basicdata_role_permission WHERE role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") String roleId);

    @Delete("DELETE FROM basicdata_role_permission WHERE permission_id = #{permissionId}")
    void deleteByPermissionId(@Param("permissionId") String permissionId);
}