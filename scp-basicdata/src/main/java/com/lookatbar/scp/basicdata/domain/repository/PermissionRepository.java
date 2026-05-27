package com.lookatbar.scp.basicdata.domain.repository;

import com.lookatbar.scp.basicdata.domain.model.permission.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository {

    Permission save(Permission permission);

    Optional<Permission> findById(String id);

    Optional<Permission> findByCode(String code);

    List<Permission> findAll();

    List<Permission> findByStatus(Integer status);

    List<Permission> findByType(Integer type);

    List<Permission> findByRoleId(String roleId);

    List<Permission> findByUserId(String userId);

    List<Permission> findByParentId(String parentId);

    void deleteById(String id);

    boolean existsByCode(String code);

    void addRolePermission(String roleId, String permissionId);

    void removeRolePermission(String roleId, String permissionId);

    void addUserRole(String userId, String roleId);

    void removeUserRole(String userId, String roleId);
}