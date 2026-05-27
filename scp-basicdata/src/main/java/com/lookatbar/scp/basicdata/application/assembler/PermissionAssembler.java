package com.lookatbar.scp.basicdata.application.assembler;

import com.lookatbar.scp.basicdata.application.dto.PermissionCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionUpdateDTO;
import com.lookatbar.scp.basicdata.domain.model.permission.Permission;
import com.lookatbar.scp.basicdata.domain.model.permission.PermissionStatus;
import com.lookatbar.scp.basicdata.domain.model.permission.PermissionType;
import org.springframework.stereotype.Component;

@Component
public class PermissionAssembler {

    public Permission toDomain(PermissionCreateDTO dto) {
        return Permission.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .type(dto.getType() != null ? PermissionType.fromCode(dto.getType()) : PermissionType.API)
                .parentId(dto.getParentId())
                .path(dto.getPath())
                .method(dto.getMethod())
                .description(dto.getDescription())
                .sortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0)
                .status(PermissionStatus.ENABLED)
                .build();
    }

    public Permission toDomain(String id, PermissionUpdateDTO dto) {
        Permission permission = Permission.builder()
                .id(id)
                .parentId(dto.getParentId())
                .path(dto.getPath())
                .method(dto.getMethod())
                .description(dto.getDescription())
                .sortOrder(dto.getSortOrder())
                .build();

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            permission.setName(dto.getName());
        }

        if (dto.getCode() != null && !dto.getCode().isEmpty()) {
            permission.setCode(dto.getCode());
        }

        if (dto.getType() != null) {
            permission.setType(PermissionType.fromCode(dto.getType()));
        }

        if (dto.getStatus() != null) {
            permission.setStatus(PermissionStatus.fromCode(dto.getStatus()));
        }

        return permission;
    }

    public PermissionDTO toDTO(Permission permission) {
        return PermissionDTO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .code(permission.getCode())
                .type(permission.getType() != null ? permission.getType().getCode() : null)
                .typeDescription(permission.getType() != null ? permission.getType().getDescription() : null)
                .parentId(permission.getParentId())
                .path(permission.getPath())
                .method(permission.getMethod())
                .description(permission.getDescription())
                .sortOrder(permission.getSortOrder())
                .status(permission.getStatus() != null ? permission.getStatus().getCode() : null)
                .statusDescription(permission.getStatus() != null ? permission.getStatus().getDescription() : null)
                .createdAt(permission.getCreatedTime())
                .updatedAt(permission.getModifiedTime())
                .build();
    }
}