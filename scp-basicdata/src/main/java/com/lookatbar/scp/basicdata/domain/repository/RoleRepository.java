package com.lookatbar.scp.basicdata.domain.repository;

import com.lookatbar.scp.basicdata.domain.model.role.Role;

import java.util.List;
import java.util.Optional;

/**
 * 角色仓储接口
 * 定义角色聚合根的数据访问操作，包括角色继承关系管理
 */
public interface RoleRepository {

    /**
     * 保存角色实体
     *
     * @param role 角色实体
     * @return 保存后的角色实体
     */
    Role save(Role role);

    /**
     * 根据ID查询角色
     *
     * @param id 角色ID
     * @return 角色实体（可选）
     */
    Optional<Role> findById(String id);

    /**
     * 根据编码查询角色
     *
     * @param code 角色编码
     * @return 角色实体（可选）
     */
    Optional<Role> findByCode(String code);

    /**
     * 根据名称查询角色
     *
     * @param name 角色名称
     * @return 角色实体（可选）
     */
    Optional<Role> findByName(String name);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    List<Role> findAll();

    /**
     * 根据状态查询角色
     *
     * @param status 角色状态
     * @return 角色列表
     */
    List<Role> findByStatus(Integer status);

    /**
     * 根据用户ID查询用户关联的角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> findByUserId(String userId);

    /**
     * 查询角色的父角色列表（角色继承）
     *
     * @param roleId 角色ID
     * @return 父角色列表
     */
    List<Role> findParentRoles(String roleId);

    /**
     * 查询角色的子角色列表（角色继承）
     *
     * @param roleId 角色ID
     * @return 子角色列表
     */
    List<Role> findChildRoles(String roleId);

    /**
     * 根据ID删除角色
     *
     * @param id 角色ID
     */
    void deleteById(String id);

    /**
     * 检查角色编码是否已存在
     *
     * @param code 角色编码
     * @return 是否存在
     */
    boolean existsByCode(String code);

    /**
     * 检查角色名称是否已存在
     *
     * @param name 角色名称
     * @return 是否存在
     */
    boolean existsByName(String name);

    /**
     * 添加角色继承关系
     *
     * @param parentRoleId 父角色ID
     * @param childRoleId  子角色ID
     */
    void addRoleHierarchy(String parentRoleId, String childRoleId);

    /**
     * 移除角色继承关系
     *
     * @param parentRoleId 父角色ID
     * @param childRoleId  子角色ID
     */
    void removeRoleHierarchy(String parentRoleId, String childRoleId);
}