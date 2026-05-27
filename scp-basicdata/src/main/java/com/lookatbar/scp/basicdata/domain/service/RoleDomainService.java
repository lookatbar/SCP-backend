package com.lookatbar.scp.basicdata.domain.service;

import com.lookatbar.scp.basicdata.domain.model.permission.Permission;
import com.lookatbar.scp.basicdata.domain.model.role.Role;
import com.lookatbar.scp.basicdata.domain.repository.PermissionRepository;
import com.lookatbar.scp.basicdata.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色领域服务
 * 处理角色相关的核心业务逻辑，包括角色创建、更新、删除、权限分配、角色继承等
 */
@Service
@RequiredArgsConstructor
public class RoleDomainService {

    /**
     * 角色仓储接口
     */
    private final RoleRepository roleRepository;

    /**
     * 权限仓储接口
     */
    private final PermissionRepository permissionRepository;

    /**
     * 创建角色
     * 
     * @param role 角色领域对象
     * @return 创建成功的角色对象
     * @throws IllegalArgumentException 当角色编码或名称已存在时抛出
     */
    @Transactional
    public Role createRole(Role role) {
        if (roleRepository.existsByCode(role.getCode())) {
            throw new IllegalArgumentException("角色编码已存在");
        }
        if (roleRepository.existsByName(role.getName())) {
            throw new IllegalArgumentException("角色名称已存在");
        }
        return roleRepository.save(role);
    }

    /**
     * 更新角色信息
     * 
     * @param role 角色领域对象
     * @return 更新后的角色对象
     * @throws IllegalArgumentException 当角色不存在或编码/名称重复时抛出
     */
    @Transactional
    public Role updateRole(Role role) {
        Role existing = roleRepository.findById(role.getId())
                .orElseThrow(() -> new IllegalArgumentException("角色不存在"));

        if (!existing.getCode().equals(role.getCode()) 
                && roleRepository.existsByCode(role.getCode())) {
            throw new IllegalArgumentException("角色编码已存在");
        }
        if (!existing.getName().equals(role.getName()) 
                && roleRepository.existsByName(role.getName())) {
            throw new IllegalArgumentException("角色名称已存在");
        }

        return roleRepository.save(role);
    }

    /**
     * 删除角色
     * 
     * @param roleId 角色ID
     * @throws IllegalArgumentException 当角色不存在时抛出
     */
    @Transactional
    public void deleteRole(String roleId) {
        if (!roleRepository.findById(roleId).isPresent()) {
            throw new IllegalArgumentException("角色不存在");
        }
        roleRepository.deleteById(roleId);
    }

    /**
     * 为角色分配单个权限
     * 
     * @param roleId       角色ID
     * @param permissionId 权限ID
     * @throws IllegalArgumentException 当角色或权限不存在时抛出
     */
    @Transactional
    public void assignPermission(String roleId, String permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在"));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("权限不存在"));

        permissionRepository.addRolePermission(roleId, permissionId);
    }

    /**
     * 撤销角色的指定权限
     * 
     * @param roleId       角色ID
     * @param permissionId 权限ID
     */
    @Transactional
    public void revokePermission(String roleId, String permissionId) {
        permissionRepository.removeRolePermission(roleId, permissionId);
    }

    /**
     * 为角色批量分配权限
     * 
     * @param roleId       角色ID
     * @param permissionIds 权限ID列表
     */
    @Transactional
    public void assignPermissions(String roleId, List<String> permissionIds) {
        permissionIds.forEach(permissionId -> assignPermission(roleId, permissionId));
    }

    /**
     * 设置角色继承关系
     * 子角色将继承父角色的所有权限
     * 
     * @param parentRoleId 父角色ID
     * @param childRoleId  子角色ID
     * @throws IllegalArgumentException 当角色不存在或尝试继承自身时抛出
     */
    @Transactional
    public void inheritRole(String parentRoleId, String childRoleId) {
        Role parentRole = roleRepository.findById(parentRoleId)
                .orElseThrow(() -> new IllegalArgumentException("父角色不存在"));
        Role childRole = roleRepository.findById(childRoleId)
                .orElseThrow(() -> new IllegalArgumentException("子角色不存在"));

        if (parentRoleId.equals(childRoleId)) {
            throw new IllegalArgumentException("角色不能继承自身");
        }

        roleRepository.addRoleHierarchy(parentRoleId, childRoleId);
    }

    /**
     * 移除角色继承关系
     * 
     * @param parentRoleId 父角色ID
     * @param childRoleId  子角色ID
     */
    @Transactional
    public void removeInheritance(String parentRoleId, String childRoleId) {
        roleRepository.removeRoleHierarchy(parentRoleId, childRoleId);
    }

    /**
     * 检查角色是否具有指定权限（含继承权限）
     * 
     * @param roleId         角色ID
     * @param permissionCode 权限编码
     * @return 是否具有该权限
     */
    public boolean hasPermission(String roleId, String permissionCode) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            return false;
        }
        return role.hasPermission(permissionCode);
    }

    /**
     * 获取角色的权限列表（含继承权限）
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    public List<Permission> getRolePermissions(String roleId) {
        return permissionRepository.findByRoleId(roleId);
    }
}