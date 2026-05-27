package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.RolePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RoleMapper extends BaseMapper<RolePO> {

    @Select("SELECT * FROM basicdata_role WHERE code = #{code}")
    Optional<RolePO> findByCode(@Param("code") String code);

    @Select("SELECT * FROM basicdata_role WHERE name = #{name}")
    Optional<RolePO> findByName(@Param("name") String name);

    @Select("SELECT * FROM basicdata_role WHERE status = #{status}")
    List<RolePO> findByStatus(@Param("status") Integer status);

    @Select("SELECT r.* FROM basicdata_role r " +
            "JOIN basicdata_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<RolePO> findByUserId(@Param("userId") String userId);

    @Select("SELECT r.* FROM basicdata_role r " +
            "JOIN basicdata_role_hierarchy rh ON r.id = rh.parent_role_id " +
            "WHERE rh.child_role_id = #{roleId}")
    List<RolePO> findParentRoles(@Param("roleId") String roleId);

    @Select("SELECT r.* FROM basicdata_role r " +
            "JOIN basicdata_role_hierarchy rh ON r.id = rh.child_role_id " +
            "WHERE rh.parent_role_id = #{roleId}")
    List<RolePO> findChildRoles(@Param("roleId") String roleId);

    @Select("SELECT COUNT(*) FROM basicdata_role WHERE code = #{code}")
    int countByCode(@Param("code") String code);

    @Select("SELECT COUNT(*) FROM basicdata_role WHERE name = #{name}")
    int countByName(@Param("name") String name);
}