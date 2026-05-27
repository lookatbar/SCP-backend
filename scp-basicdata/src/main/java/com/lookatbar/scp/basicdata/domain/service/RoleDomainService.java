package com.lookatbar.scp.basicdata.domain.service;

import com.lookatbar.scp.basicdata.domain.model.permission.Permission;
import com.lookatbar.scp.basicdata.domain.model.role.Role;
import com.lookatbar.scp.basicdata.domain.repository.PermissionRepository;
import com.lookatbar.scp.basicdata.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleDomainService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

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

    @Transactional
    public void deleteRole(String roleId) {
        if (!roleRepository.findById(roleId).isPresent()) {
            throw new IllegalArgumentException("角色不存在");
        }
        roleRepository.deleteById(roleId);
    }

    @Transactional
    public void assignPermission(String roleId, String permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在"));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("权限不存在"));

        permissionRepository.addRolePermission(roleId, permissionId);
    }

    @Transactional
    public void revokePermission(String roleId, String permissionId) {
        permissionRepository.removeRolePermission(roleId, permissionId);
    }

    @Transactional
    public void assignPermissions(String roleId, List<String> permissionIds) {
        permissionIds.forEach(permissionId -> assignPermission(roleId, permissionId));
    }

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

    @Transactional
    public void removeInheritance(String parentRoleId, String childRoleId) {
        roleRepository.removeRoleHierarchy(parentRoleId, childRoleId);
    }

    public boolean hasPermission(String roleId, String permissionCode) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            return false;
        }
        return role.hasPermission(permissionCode);
    }

    public List<Permission> getRolePermissions(String roleId) {
        return permissionRepository.findByRoleId(roleId);
    }
}