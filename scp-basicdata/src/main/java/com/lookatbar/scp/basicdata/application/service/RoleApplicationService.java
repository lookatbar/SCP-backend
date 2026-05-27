package com.lookatbar.scp.basicdata.application.service;

import com.lookatbar.scp.basicdata.application.assembler.PermissionAssembler;
import com.lookatbar.scp.basicdata.application.assembler.RoleAssembler;
import com.lookatbar.scp.basicdata.application.dto.AssignPermissionDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleUpdateDTO;
import com.lookatbar.scp.basicdata.domain.model.role.Role;
import com.lookatbar.scp.basicdata.domain.repository.RoleRepository;
import com.lookatbar.scp.basicdata.domain.service.RoleDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleApplicationService {

    private final RoleDomainService roleDomainService;
    private final RoleRepository roleRepository;
    private final RoleAssembler roleAssembler;
    private final PermissionAssembler permissionAssembler;

    @Transactional
    public RoleDTO createRole(RoleCreateDTO dto) {
        Role role = roleAssembler.toDomain(dto);
        Role savedRole = roleDomainService.createRole(role);
        return roleAssembler.toDTO(savedRole);
    }

    @Transactional
    public RoleDTO updateRole(String id, RoleUpdateDTO dto) {
        Role role = roleAssembler.toDomain(id, dto);
        Role updatedRole = roleDomainService.updateRole(role);
        return roleAssembler.toDTO(updatedRole);
    }

    @Transactional
    public void deleteRole(String id) {
        roleDomainService.deleteRole(id);
    }

    public RoleDTO getRoleById(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在"));
        RoleDTO dto = roleAssembler.toDTO(role);
        dto.setPermissions(roleDomainService.getRolePermissions(id).stream()
                .map(permissionAssembler::toDTO)
                .collect(Collectors.toList()));
        dto.setParentRoles(roleRepository.findParentRoles(id).stream()
                .map(roleAssembler::toDTO)
                .collect(Collectors.toList()));
        dto.setChildRoles(roleRepository.findChildRoles(id).stream()
                .map(roleAssembler::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> {
                    RoleDTO dto = roleAssembler.toDTO(role);
                    dto.setPermissions(roleDomainService.getRolePermissions(role.getId()).stream()
                            .map(permissionAssembler::toDTO)
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void assignPermissions(String roleId, AssignPermissionDTO dto) {
        roleDomainService.assignPermissions(roleId, dto.getPermissionIds());
    }

    @Transactional
    public void revokePermission(String roleId, String permissionId) {
        roleDomainService.revokePermission(roleId, permissionId);
    }

    @Transactional
    public void inheritRole(String parentRoleId, String childRoleId) {
        roleDomainService.inheritRole(parentRoleId, childRoleId);
    }

    @Transactional
    public void removeInheritance(String parentRoleId, String childRoleId) {
        roleDomainService.removeInheritance(parentRoleId, childRoleId);
    }

    public boolean hasPermission(String roleId, String permissionCode) {
        return roleDomainService.hasPermission(roleId, permissionCode);
    }
}