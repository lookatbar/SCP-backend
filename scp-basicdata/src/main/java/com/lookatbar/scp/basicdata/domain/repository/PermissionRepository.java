package com.lookatbar.scp.basicdata.domain.repository;

import com.lookatbar.scp.basicdata.domain.model.permission.Permission;

import java.util.List;
import java.util.Optional;

/**
 * 权限仓储接口
 * 定义权限实体的数据访问操作，包括权限分配关系管理
 */
public interface PermissionRepository {

    /**
     * 保存权限实体
     *
     * @param permission 权限实体
     * @return 保存后的权限实体
     */
    Permission save(Permission permission);

    /**
     * 根据ID查询权限
     *
     * @param id 权限ID
     * @return 权限实体（可选）
     */
    Optional<Permission> findById(String id);

    /**
     * 根据编码查询权限
     *
     * @param code 权限编码
     * @return 权限实体（可选）
     */
    Optional<Permission> findByCode(String code);

    /**
     * 查询所有权限
     *
     * @return 权限列表
     */
    List<Permission> findAll();

    /**
     * 根据状态查询权限
     *
     * @param status 权限状态
     * @return 权限列表
     */
    List<Permission> findByStatus(Integer status);

    /**
     * 根据类型查询权限
     *
     * @param type 权限类型
     * @return 权限列表
     */
    List<Permission> findByType(Integer type);

    /**
     * 查询角色拥有的权限列表（含继承权限）
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> findByRoleId(String roleId);

    /**
     * 查询用户拥有的权限列表（含角色继承权限）
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> findByUserId(String userId);

    /**
     * 根据父权限ID查询子权限列表
     *
     * @param parentId 父权限ID
     * @return 子权限列表
     */
    List<Permission> findByParentId(String parentId);

    /**
     * 根据ID删除权限
     *
     * @param id 权限ID
     */
    void deleteById(String id);

    /**
     * 检查权限编码是否已存在
     *
     * @param code 权限编码
     * @return 是否存在
     */
    boolean existsByCode(String code);

    /**
     * 为角色分配权限
     *
     * @param roleId       角色ID
     * @param permissionId 权限ID
     */
    void addRolePermission(String roleId, String permissionId);

    /**
     * 撤销角色的权限
     *
     * @param roleId       角色ID
     * @param permissionId 权限ID
     */
    void removeRolePermission(String roleId, String permissionId);

    /**
     * 为用户分配角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void addUserRole(String userId, String roleId);

    /**
     * 撤销用户的角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void removeUserRole(String userId, String roleId);
}