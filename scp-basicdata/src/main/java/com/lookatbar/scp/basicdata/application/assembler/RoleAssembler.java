package com.lookatbar.scp.basicdata.application.assembler;

import com.lookatbar.scp.basicdata.application.dto.RoleCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleUpdateDTO;
import com.lookatbar.scp.basicdata.domain.model.role.Role;
import com.lookatbar.scp.basicdata.domain.model.role.RoleStatus;
import org.springframework.stereotype.Component;

@Component
public class RoleAssembler {

    public Role toDomain(RoleCreateDTO dto) {
        return Role.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .status(RoleStatus.ENABLED)
                .build();
    }

    public Role toDomain(Long id, RoleUpdateDTO dto) {
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

    public RoleDTO toDTO(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .code(role.getCode())
                .description(role.getDescription())
                .status(role.getStatus() != null ? role.getStatus().getCode() : null)
                .statusDescription(role.getStatus() != null ? role.getStatus().getDescription() : null)
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }
}