package com.lookatbar.scp.basicdata.application.service;

import com.lookatbar.scp.basicdata.application.assembler.PermissionAssembler;
import com.lookatbar.scp.basicdata.application.dto.PermissionCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionUpdateDTO;
import com.lookatbar.scp.basicdata.domain.model.permission.Permission;
import com.lookatbar.scp.basicdata.domain.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionApplicationService {

    private final PermissionRepository permissionRepository;
    private final PermissionAssembler permissionAssembler;

    @Transactional
    public PermissionDTO createPermission(PermissionCreateDTO dto) {
        Permission permission = permissionAssembler.toDomain(dto);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionAssembler.toDTO(savedPermission);
    }

    @Transactional
    public PermissionDTO updatePermission(Long id, PermissionUpdateDTO dto) {
        Permission permission = permissionAssembler.toDomain(id, dto);
        Permission updatedPermission = permissionRepository.save(permission);
        return permissionAssembler.toDTO(updatedPermission);
    }

    @Transactional
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }

    public PermissionDTO getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("权限不存在"));
        return permissionAssembler.toDTO(permission);
    }

    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionAssembler::toDTO)
                .collect(Collectors.toList());
    }

    public List<PermissionDTO> getPermissionsByType(Integer type) {
        return permissionRepository.findByType(type).stream()
                .map(permissionAssembler::toDTO)
                .collect(Collectors.toList());
    }

    public List<PermissionDTO> getPermissionsByRoleId(Long roleId) {
        return permissionRepository.findByRoleId(roleId).stream()
                .map(permissionAssembler::toDTO)
                .collect(Collectors.toList());
    }

    public List<PermissionDTO> getPermissionsByUserId(Long userId) {
        return permissionRepository.findByUserId(userId).stream()
                .map(permissionAssembler::toDTO)
                .collect(Collectors.toList());
    }

    public List<PermissionDTO> getPermissionTree() {
        List<Permission> allPermissions = permissionRepository.findAll();
        List<Permission> rootPermissions = allPermissions.stream()
                .filter(p -> p.getParentId() == null || p.getParentId() == 0)
                .collect(Collectors.toList());

        return buildPermissionTree(rootPermissions, allPermissions);
    }

    private List<PermissionDTO> buildPermissionTree(List<Permission> parents, List<Permission> allPermissions) {
        List<PermissionDTO> result = new ArrayList<>();

        for (Permission parent : parents) {
            PermissionDTO dto = permissionAssembler.toDTO(parent);
            List<Permission> children = allPermissions.stream()
                    .filter(p -> p.getParentId() != null && p.getParentId().equals(parent.getId()))
                    .collect(Collectors.toList());

            if (!children.isEmpty()) {
                dto.setChildren(buildPermissionTree(children, allPermissions));
            }

            result.add(dto);
        }

        return result;
    }
}