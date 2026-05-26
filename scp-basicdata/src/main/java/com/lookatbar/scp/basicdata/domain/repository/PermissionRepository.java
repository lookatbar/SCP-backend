package com.lookatbar.scp.basicdata.domain.repository;

import com.lookatbar.scp.basicdata.domain.model.permission.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository {

    Permission save(Permission permission);

    Optional<Permission> findById(Long id);

    Optional<Permission> findByCode(String code);

    List<Permission> findAll();

    List<Permission> findByStatus(Integer status);

    List<Permission> findByType(Integer type);

    List<Permission> findByRoleId(Long roleId);

    List<Permission> findByUserId(Long userId);

    List<Permission> findByParentId(Long parentId);

    void deleteById(Long id);

    boolean existsByCode(String code);

    void addRolePermission(Long roleId, Long permissionId);

    void removeRolePermission(Long roleId, Long permissionId);

    void addUserRole(Long userId, Long roleId);

    void removeUserRole(Long userId, Long roleId);
}