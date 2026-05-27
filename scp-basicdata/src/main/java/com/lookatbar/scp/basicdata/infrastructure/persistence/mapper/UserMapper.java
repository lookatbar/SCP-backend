package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * 用户Mapper接口
 * 基于MyBatis Plus实现用户数据访问
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPO> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户PO
     */
    @Select("SELECT * FROM basicdata_user WHERE username = #{username}")
    Optional<UserPO> findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户PO
     */
    @Select("SELECT * FROM basicdata_user WHERE email = #{email}")
    Optional<UserPO> findByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户PO
     */
    @Select("SELECT * FROM basicdata_user WHERE phone = #{phone}")
    Optional<UserPO> findByPhone(@Param("phone") String phone);

    /**
     * 根据状态查询用户列表
     *
     * @param status 状态
     * @return 用户PO列表
     */
    @Select("SELECT * FROM basicdata_user WHERE status = #{status}")
    List<UserPO> findByStatus(@Param("status") Integer status);

    /**
     * 根据用户名统计数量
     *
     * @param username 用户名
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM basicdata_user WHERE username = #{username}")
    int countByUsername(@Param("username") String username);

    /**
     * 根据邮箱统计数量
     *
     * @param email 邮箱
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM basicdata_user WHERE email = #{email}")
    int countByEmail(@Param("email") String email);

    /**
     * 根据手机号统计数量
     *
     * @param phone 手机号
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM basicdata_user WHERE phone = #{phone}")
    int countByPhone(@Param("phone") String phone);
}