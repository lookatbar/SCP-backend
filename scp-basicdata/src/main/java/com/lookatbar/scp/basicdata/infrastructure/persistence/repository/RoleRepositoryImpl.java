package com.lookatbar.scp.basicdata.infrastructure.persistence.repository;

import com.lookatbar.scp.basicdata.domain.model.role.Role;
import com.lookatbar.scp.basicdata.domain.model.role.RoleStatus;
import com.lookatbar.scp.basicdata.domain.repository.RoleRepository;
import com.lookatbar.scp.basicdata.infrastructure.persistence.mapper.RoleHierarchyMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.mapper.RoleMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.RolePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleMapper roleMapper;
    private final RoleHierarchyMapper roleHierarchyMapper;

    @Override
    public Role save(Role role) {
        RolePO po = toPO(role);
        if (role.getId() == null) {
            roleMapper.insert(po);
        } else {
            roleMapper.updateById(po);
        }
        return toDomain(po);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleMapper.selectById(id).map(this::toDomain);
    }

    @Override
    public Optional<Role> findByCode(String code) {
        return roleMapper.findByCode(code).map(this::toDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleMapper.findByName(name).map(this::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return roleMapper.selectList(null).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> findByStatus(Integer status) {
        return roleMapper.findByStatus(status).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> findByUserId(Long userId) {
        return roleMapper.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> findParentRoles(Long roleId) {
        return roleMapper.findParentRoles(roleId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> findChildRoles(Long roleId) {
        return roleMapper.findChildRoles(roleId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        roleMapper.deleteById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return roleMapper.countByCode(code) > 0;
    }

    @Override
    public boolean existsByName(String name) {
        return roleMapper.countByName(name) > 0;
    }

    @Override
    public void addRoleHierarchy(Long parentRoleId, Long childRoleId) {
        roleHierarchyMapper.insertRoleHierarchy(parentRoleId, childRoleId);
    }

    @Override
    public void removeRoleHierarchy(Long parentRoleId, Long childRoleId) {
        roleHierarchyMapper.deleteRoleHierarchy(parentRoleId, childRoleId);
    }

    private RolePO toPO(Role role) {
        return RolePO.builder()
                .id(role.getId())
                .name(role.getName())
                .code(role.getCode())
                .description(role.getDescription())
                .status(role.getStatus() != null ? role.getStatus().getCode() : null)
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }

    private Role toDomain(RolePO po) {
        return Role.builder()
                .id(po.getId())
                .name(po.getName())
                .code(po.getCode())
                .description(po.getDescription())
                .status(po.getStatus() != null ? RoleStatus.fromCode(po.getStatus()) : null)
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }
}