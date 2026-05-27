package com.lookatbar.scp.basicdata.domain.repository;

import com.lookatbar.scp.basicdata.domain.model.role.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(String id);

    Optional<Role> findByCode(String code);

    Optional<Role> findByName(String name);

    List<Role> findAll();

    List<Role> findByStatus(Integer status);

    List<Role> findByUserId(String userId);

    List<Role> findParentRoles(String roleId);

    List<Role> findChildRoles(String roleId);

    void deleteById(String id);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    void addRoleHierarchy(String parentRoleId, String childRoleId);

    void removeRoleHierarchy(String parentRoleId, String childRoleId);
}