package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.UserRolePO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户角色关联Mapper接口
 * 基于MyBatis Plus实现用户角色关联数据访问
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRolePO> {

    /**
     * 插入用户角色关联
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    @Insert("INSERT INTO basicdata_user_role (user_id, role_id) VALUES (#{userId}, #{roleId})")
    void insertUserRole(@Param("userId") String userId, @Param("roleId") String roleId);

    /**
     * 删除用户角色关联
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    @Delete("DELETE FROM basicdata_user_role WHERE user_id = #{userId} AND role_id = #{roleId}")
    void deleteUserRole(@Param("userId") String userId, @Param("roleId") String roleId);

    /**
     * 根据用户ID删除所有关联
     *
     * @param userId 用户ID
     */
    @Delete("DELETE FROM basicdata_user_role WHERE user_id = #{userId}")
    void deleteByUserId(@Param("userId") String userId);

    /**
     * 根据角色ID删除所有关联
     *
     * @param roleId 角色ID
     */
    @Delete("DELETE FROM basicdata_user_role WHERE role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") String roleId);
}