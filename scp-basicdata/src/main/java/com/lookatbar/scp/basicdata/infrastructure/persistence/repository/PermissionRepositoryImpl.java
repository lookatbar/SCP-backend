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

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryImpl implements PermissionRepository {

    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final UserRoleMapper userRoleMapper;

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

    @Override
    public Optional<Permission> findById(String id) {
        return Optional.ofNullable(this.toDomain(permissionMapper.selectById(id)));
    }

    @Override
    public Optional<Permission> findByCode(String code) {
        return permissionMapper.findByCode(code).map(this::toDomain);
    }

    @Override
    public List<Permission> findAll() {
        return permissionMapper.selectList(null).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> findByStatus(Integer status) {
        return permissionMapper.findByStatus(status).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> findByType(Integer type) {
        return permissionMapper.findByType(type).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> findByRoleId(String roleId) {
        return permissionMapper.findByRoleId(roleId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> findByUserId(String userId) {
        return permissionMapper.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> findByParentId(String parentId) {
        return permissionMapper.findByParentId(parentId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        permissionMapper.deleteById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return permissionMapper.countByCode(code) > 0;
    }

    @Override
    public void addRolePermission(String roleId, String permissionId) {
        rolePermissionMapper.insertRolePermission(roleId, permissionId);
    }

    @Override
    public void removeRolePermission(String roleId, String permissionId) {
        rolePermissionMapper.deleteRolePermission(roleId, permissionId);
    }

    @Override
    public void addUserRole(String userId, String roleId) {
        userRoleMapper.insertUserRole(userId, roleId);
    }

    @Override
    public void removeUserRole(String userId, String roleId) {
        userRoleMapper.deleteUserRole(userId, roleId);
    }

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