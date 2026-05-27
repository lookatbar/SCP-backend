package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper extends BaseMapper<UserPO> {

    @Select("SELECT * FROM basicdata_user WHERE username = #{username}")
    Optional<UserPO> findByUsername(@Param("username") String username);

    @Select("SELECT * FROM basicdata_user WHERE email = #{email}")
    Optional<UserPO> findByEmail(@Param("email") String email);

    @Select("SELECT * FROM basicdata_user WHERE phone = #{phone}")
    Optional<UserPO> findByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM basicdata_user WHERE status = #{status}")
    List<UserPO> findByStatus(@Param("status") Integer status);

    @Select("SELECT COUNT(*) FROM basicdata_user WHERE username = #{username}")
    int countByUsername(@Param("username") String username);

    @Select("SELECT COUNT(*) FROM basicdata_user WHERE email = #{email}")
    int countByEmail(@Param("email") String email);

    @Select("SELECT COUNT(*) FROM basicdata_user WHERE phone = #{phone}")
    int countByPhone(@Param("phone") String phone);
}