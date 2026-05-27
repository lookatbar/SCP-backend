package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.RoleHierarchyPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface RoleHierarchyMapper extends BaseMapper<RoleHierarchyPO> {

    @Insert("INSERT INTO basicdata_role_hierarchy (parent_role_id, child_role_id) VALUES (#{parentRoleId}, #{childRoleId})")
    void insertRoleHierarchy(@Param("parentRoleId") String parentRoleId, @Param("childRoleId") String childRoleId);

    @Delete("DELETE FROM basicdata_role_hierarchy WHERE parent_role_id = #{parentRoleId} AND child_role_id = #{childRoleId}")
    void deleteRoleHierarchy(@Param("parentRoleId") String parentRoleId, @Param("childRoleId") String childRoleId);

    @Delete("DELETE FROM basicdata_role_hierarchy WHERE parent_role_id = #{roleId}")
    void deleteByParentRoleId(@Param("roleId") String roleId);

    @Delete("DELETE FROM basicdata_role_hierarchy WHERE child_role_id = #{roleId}")
    void deleteByChildRoleId(@Param("roleId") String roleId);
}