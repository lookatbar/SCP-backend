package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.RoleHierarchyPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface RoleHierarchyMapper extends BaseMapper<RoleHierarchyPO> {

    @Insert("INSERT INTO rbac_role_hierarchy (parent_role_id, child_role_id) VALUES (#{parentRoleId}, #{childRoleId})")
    void insertRoleHierarchy(@Param("parentRoleId") Long parentRoleId, @Param("childRoleId") Long childRoleId);

    @Delete("DELETE FROM rbac_role_hierarchy WHERE parent_role_id = #{parentRoleId} AND child_role_id = #{childRoleId}")
    void deleteRoleHierarchy(@Param("parentRoleId") Long parentRoleId, @Param("childRoleId") Long childRoleId);

    @Delete("DELETE FROM rbac_role_hierarchy WHERE parent_role_id = #{roleId}")
    void deleteByParentRoleId(@Param("roleId") Long roleId);

    @Delete("DELETE FROM rbac_role_hierarchy WHERE child_role_id = #{roleId}")
    void deleteByChildRoleId(@Param("roleId") Long roleId);
}