package com.lookatbar.scp.basicdata.infrastructure.persistence.repository;

import com.lookatbar.scp.basicdata.domain.model.permission.Permission;
import com.lookatbar.scp.basicdata.domain.model.permission.PermissionStatus;
import com.lookatbar.scp.basicdata.domain.model.permission.PermissionType;
import com.lookatbar.scp.basicdata.domain.repository.PermissionRepository;
import com.lookatbar.scp.basicdata.infrastructure.persistence.mapper.PermissionMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.mapper.RolePermissionMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.mapper.UserRoleMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.PermissionPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 权限仓储实现类
 * 基于MyBatis Plus实现权限数据访问，包括权限分配关系管理
 */
@Repository
@RequiredArgsConstructor
public class PermissionRepositoryImpl implements PermissionRepository {

    /**
     * 权限数据访问Mapper
     */
    private final PermissionMapper permissionMapper;

    /**
     * 角色权限关系Mapper
     */
    private final RolePermissionMapper rolePermissionMapper;

    /**
     * 用户角色关系Mapper
     */
    private final UserRoleMapper userRoleMapper;

    /**
     * 保存权限实体
     *
     * @param permission 权限实体
     * @return 保存后的权限实体
     */
    @Override
    public Permission save(Permission permission) {
        PermissionPO po = toPO(permission);
        if (permission.getId() == null) {
            permissionMapper.insert(po);
        } else {
            permissionMapper.updateById(po);
        }
        return toDomain(po);
    }

    /**
     * 根据ID查询权限
     *
     * @param id 权限ID
     * @return 权限实体（可选）
     */
    @Override
    public Optional<Permission> findById(String id) {
        return Optional.ofNullable(this.toDomain(permissionMapper.selectById(id)));
    }

    /**
     * 根据编码查询权限
     *
     * @param code 权限编码
     * @return 权限实体（可选）
     */
    @Override
    public Optional<Permission> findByCode(String code) {
        return permissionMapper.findByCode(code).map(this::toDomain);
    }

    /**
     * 查询所有权限
     *
     * @return 权限列表
     */
    @Override
    public List<Permission> findAll() {
        return permissionMapper.selectList(null).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 根据状态查询权限
     *
     * @param status 权限状态
     * @return 权限列表
     */
    @Override
    public List<Permission> findByStatus(Integer status) {
        return permissionMapper.findByStatus(status).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 根据类型查询权限
     *
     * @param type 权限类型
     * @return 权限列表
     */
    @Override
    public List<Permission> findByType(Integer type) {
        return permissionMapper.findByType(type).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 查询角色拥有的权限列表（含继承权限）
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public List<Permission> findByRoleId(String roleId) {
        return permissionMapper.findByRoleId(roleId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 查询用户拥有的权限列表（含角色继承权限）
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public List<Permission> findByUserId(String userId) {
        return permissionMapper.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 根据父权限ID查询子权限列表
     *
     * @param parentId 父权限ID
     * @return 子权限列表
     */
    @Override
    public List<Permission> findByParentId(String parentId) {
        return permissionMapper.findByParentId(parentId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID删除权限
     *
     * @param id 权限ID
     */
    @Override
    public void deleteById(String id) {
        permissionMapper.deleteById(id);
    }

    /**
     * 检查权限编码是否已存在
     *
     * @param code 权限编码
     * @return 是否存在
     */
    @Override
    public boolean existsByCode(String code) {
        return permissionMapper.countByCode(code) > 0;
    }

    /**
     * 为角色分配权限
     *
     * @param roleId       角色ID
     * @param permissionId 权限ID
     */
    @Override
    public void addRolePermission(String roleId, String permissionId) {
        rolePermissionMapper.insertRolePermission(roleId, permissionId);
    }

    /**
     * 撤销角色的权限
     *
     * @param roleId       角色ID
     * @param permissionId 权限ID
     */
    @Override
    public void removeRolePermission(String roleId, String permissionId) {
        rolePermissionMapper.deleteRolePermission(roleId, permissionId);
    }

    /**
     * 为用户分配角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    @Override
    public void addUserRole(String userId, String roleId) {
        userRoleMapper.insertUserRole(userId, roleId);
    }

    /**
     * 撤销用户的角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    @Override
    public void removeUserRole(String userId, String roleId) {
        userRoleMapper.deleteUserRole(userId, roleId);
    }

    /**
     * 将领域模型转换为持久化对象
     *
     * @param permission 权限领域模型
     * @return 权限持久化对象
     */
    private PermissionPO toPO(Permission permission) {
        return PermissionPO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .code(permission.getCode())
                .type(permission.getType() != null ? permission.getType().getCode() : null)
                .parentId(permission.getParentId())
                .path(permission.getPath())
                .method(permission.getMethod())
                .description(permission.getDescription())
                .sortOrder(permission.getSortOrder())
                .status(permission.getStatus() != null ? permission.getStatus().getCode() : null)
                .createdTime(permission.getCreatedTime())
                .modifiedTime(permission.getModifiedTime())
                .build();
    }

    /**
     * 将持久化对象转换为领域模型
     *
     * @param po 权限持久化对象
     * @return 权限领域模型
     */
    private Permission toDomain(PermissionPO po) {
        return Permission.builder()
                .id(po.getId())
                .name(po.getName())
                .code(po.getCode())
                .type(po.getType() != null ? PermissionType.fromCode(po.getType()) : null)
                .parentId(po.getParentId())
                .path(po.getPath())
                .method(po.getMethod())
                .description(po.getDescription())
                .sortOrder(po.getSortOrder())
                .status(po.getStatus() != null ? PermissionStatus.fromCode(po.getStatus()) : null)
                .createdTime(po.getCreatedTime())
                .modifiedTime(po.getModifiedTime())
                .build();
    }
}