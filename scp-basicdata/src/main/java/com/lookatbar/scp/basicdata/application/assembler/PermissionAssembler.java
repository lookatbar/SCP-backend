package com.lookatbar.scp.basicdata.application.assembler;

import com.lookatbar.scp.basicdata.application.dto.PermissionCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionUpdateDTO;
import com.lookatbar.scp.basicdata.domain.model.permission.Permission;
import com.lookatbar.scp.basicdata.domain.model.permission.PermissionStatus;
import com.lookatbar.scp.basicdata.domain.model.permission.PermissionType;
import org.springframework.stereotype.Component;

/**
 * 权限领域对象与DTO转换器
 * 负责Permission领域模型与PermissionDTO、PermissionCreateDTO、PermissionUpdateDTO之间的转换
 */
@Component
public class PermissionAssembler {

    /**
     * 将权限创建DTO转换为领域模型
     *
     * @param dto 权限创建请求DTO
     * @return 权限领域模型
     */
    public Permission toDomain(PermissionCreateDTO dto) {
        return Permission.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .type(dto.getType() != null ? PermissionType.fromCode(dto.getType()) : PermissionType.API)
                .parentId(dto.getParentId())
                .path(dto.getPath())
                .method(dto.getMethod())
                .description(dto.getDescription())
                .extInfo(dto.getExtInfo())
                .sortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0)
                .status(PermissionStatus.ENABLED)
                .build();
    }

    /**
     * 将权限更新DTO转换为领域模型
     *
     * @param id  权限ID
     * @param dto 权限更新请求DTO
     * @return 权限领域模型
     */
    public Permission toDomain(String id, PermissionUpdateDTO dto) {
        Permission permission = Permission.builder()
                .id(id)
                .parentId(dto.getParentId())
                .path(dto.getPath())
                .method(dto.getMethod())
                .description(dto.getDescription())
                .extInfo(dto.getExtInfo())
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

    /**
     * 将权限领域模型转换为DTO
     *
     * @param permission 权限领域模型
     * @return 权限DTO
     */
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
                .extInfo(permission.getExtInfo())
                .sortOrder(permission.getSortOrder())
                .status(permission.getStatus() != null ? permission.getStatus().getCode() : null)
                .statusDescription(permission.getStatus() != null ? permission.getStatus().getDescription() : null)
                .createdAt(permission.getCreatedTime())
                .updatedAt(permission.getModifiedTime())
                .build();
    }
}