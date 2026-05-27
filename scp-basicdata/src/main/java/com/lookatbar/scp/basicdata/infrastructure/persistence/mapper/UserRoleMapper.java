package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.UserRolePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRolePO> {

    @Insert("INSERT INTO basicdata_user_role (user_id, role_id) VALUES (#{userId}, #{roleId})")
    void insertUserRole(@Param("userId") String userId, @Param("roleId") String roleId);

    @Delete("DELETE FROM basicdata_user_role WHERE user_id = #{userId} AND role_id = #{roleId}")
    void deleteUserRole(@Param("userId") String userId, @Param("roleId") String roleId);

    @Delete("DELETE FROM basicdata_user_role WHERE user_id = #{userId}")
    void deleteByUserId(@Param("userId") String userId);

    @Delete("DELETE FROM basicdata_user_role WHERE role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") String roleId);
}