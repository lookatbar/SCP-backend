package com.lookatbar.scp.basicdata.domain.repository;

import com.lookatbar.scp.basicdata.domain.model.user.User;

import java.util.List;
import java.util.Optional;

/**
 * 用户仓储接口
 * 定义用户聚合根的数据访问操作
 */
public interface UserRepository {

    /**
     * 保存用户实体
     *
     * @param user 用户实体
     * @return 保存后的用户实体
     */
    User save(User user);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户实体（可选）
     */
    Optional<User> findById(String id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户实体（可选）
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱地址
     * @return 用户实体（可选）
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号码
     * @return 用户实体（可选）
     */
    Optional<User> findByPhone(String phone);

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<User> findAll();

    /**
     * 根据状态查询用户
     *
     * @param status 用户状态
     * @return 用户列表
     */
    List<User> findByStatus(Integer status);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     */
    void deleteById(String id);

    /**
     * 检查用户名是否已存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否已存在
     *
     * @param email 邮箱地址
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 检查手机号是否已存在
     *
     * @param phone 手机号码
     * @return 是否存在
     */
    boolean existsByPhone(String phone);
}