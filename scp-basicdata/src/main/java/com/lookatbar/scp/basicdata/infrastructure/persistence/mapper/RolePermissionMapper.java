package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.RolePermissionPO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色权限关联Mapper接口
 * 基于MyBatis Plus实现角色权限关联数据访问
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermissionPO> {

    /**
     * 插入角色权限关联
     *
     * @param roleId       角色ID
     * @param permissionId 权限ID
     */
    @Insert("INSERT INTO basicdata_role_permission (role_id, permission_id) VALUES (#{roleId}, #{permissionId})")
    void insertRolePermission(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

    /**
     * 删除角色权限关联
     *
     * @param roleId       角色ID
     * @param permissionId 权限ID
     */
    @Delete("DELETE FROM basicdata_role_permission WHERE role_id = #{roleId} AND permission_id = #{permissionId}")
    void deleteRolePermission(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

    /**
     * 根据角色ID删除所有关联
     *
     * @param roleId 角色ID
     */
    @Delete("DELETE FROM basicdata_role_permission WHERE role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") String roleId);

    /**
     * 根据权限ID删除所有关联
     *
     * @param permissionId 权限ID
     */
    @Delete("DELETE FROM basicdata_role_permission WHERE permission_id = #{permissionId}")
    void deleteByPermissionId(@Param("permissionId") String permissionId);
}