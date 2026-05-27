package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.RoleHierarchyPO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色继承关系Mapper接口
 * 基于MyBatis Plus实现角色继承关系数据访问（RBAC3模型支持）
 */
@Mapper
public interface RoleHierarchyMapper extends BaseMapper<RoleHierarchyPO> {

    /**
     * 插入角色继承关系
     *
     * @param parentRoleId 父角色ID
     * @param childRoleId  子角色ID
     */
    @Insert("INSERT INTO basicdata_role_hierarchy (parent_role_id, child_role_id) VALUES (#{parentRoleId}, #{childRoleId})")
    void insertRoleHierarchy(@Param("parentRoleId") String parentRoleId, @Param("childRoleId") String childRoleId);

    /**
     * 删除角色继承关系
     *
     * @param parentRoleId 父角色ID
     * @param childRoleId  子角色ID
     */
    @Delete("DELETE FROM basicdata_role_hierarchy WHERE parent_role_id = #{parentRoleId} AND child_role_id = #{childRoleId}")
    void deleteRoleHierarchy(@Param("parentRoleId") String parentRoleId, @Param("childRoleId") String childRoleId);

    /**
     * 根据父角色ID删除所有继承关系
     *
     * @param roleId 父角色ID
     */
    @Delete("DELETE FROM basicdata_role_hierarchy WHERE parent_role_id = #{roleId}")
    void deleteByParentRoleId(@Param("roleId") String roleId);

    /**
     * 根据子角色ID删除所有继承关系
     *
     * @param roleId 子角色ID
     */
    @Delete("DELETE FROM basicdata_role_hierarchy WHERE child_role_id = #{roleId}")
    void deleteByChildRoleId(@Param("roleId") String roleId);
}