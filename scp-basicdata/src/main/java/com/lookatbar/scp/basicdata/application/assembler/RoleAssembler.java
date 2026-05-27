package com.lookatbar.scp.basicdata.application.assembler;

import com.lookatbar.scp.basicdata.application.dto.RoleCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleUpdateDTO;
import com.lookatbar.scp.basicdata.domain.model.role.Role;
import com.lookatbar.scp.basicdata.domain.model.role.RoleStatus;
import org.springframework.stereotype.Component;

/**
 * 角色领域对象与DTO转换器
 * 负责Role领域模型与RoleDTO、RoleCreateDTO、RoleUpdateDTO之间的转换
 */
@Component
public class RoleAssembler {

    /**
     * 将角色创建DTO转换为领域模型
     *
     * @param dto 角色创建请求DTO
     * @return 角色领域模型
     */
    public Role toDomain(RoleCreateDTO dto) {
        return Role.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .status(RoleStatus.ENABLED)
                .build();
    }

    /**
     * 将角色更新DTO转换为领域模型
     *
     * @param id  角色ID
     * @param dto 角色更新请求DTO
     * @return 角色领域模型
     */
    public Role toDomain(String id, RoleUpdateDTO dto) {
        Role role = Role.builder()
                .id(id)
                .description(dto.getDescription())
                .build();

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            role.setName(dto.getName());
        }

        if (dto.getCode() != null && !dto.getCode().isEmpty()) {
            role.setCode(dto.getCode());
        }

        if (dto.getStatus() != null) {
            role.setStatus(RoleStatus.fromCode(dto.getStatus()));
        }

        return role;
    }

    /**
     * 将角色领域模型转换为DTO
     *
     * @param role 角色领域模型
     * @return 角色DTO
     */
    public RoleDTO toDTO(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .code(role.getCode())
                .description(role.getDescription())
                .status(role.getStatus() != null ? role.getStatus().getCode() : null)
                .statusDescription(role.getStatus() != null ? role.getStatus().getDescription() : null)
                .createdAt(role.getCreatedTime())
                .updatedAt(role.getModifiedTime())
                .build();
    }
}