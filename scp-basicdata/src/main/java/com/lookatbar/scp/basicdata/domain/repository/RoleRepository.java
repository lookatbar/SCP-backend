package com.lookatbar.scp.basicdata.domain.repository;

import com.lookatbar.scp.basicdata.domain.model.role.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(Long id);

    Optional<Role> findByCode(String code);

    Optional<Role> findByName(String name);

    List<Role> findAll();

    List<Role> findByStatus(Integer status);

    List<Role> findByUserId(Long userId);

    List<Role> findParentRoles(Long roleId);

    List<Role> findChildRoles(Long roleId);

    void deleteById(Long id);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    void addRoleHierarchy(Long parentRoleId, Long childRoleId);

    void removeRoleHierarchy(Long parentRoleId, Long childRoleId);
}