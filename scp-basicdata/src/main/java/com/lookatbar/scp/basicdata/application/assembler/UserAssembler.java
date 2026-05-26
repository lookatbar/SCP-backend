package com.lookatbar.scp.basicdata.application.assembler;

import com.lookatbar.scp.basicdata.application.dto.UserCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.UserDTO;
import com.lookatbar.scp.basicdata.application.dto.UserUpdateDTO;
import com.lookatbar.scp.basicdata.domain.model.user.User;
import com.lookatbar.scp.basicdata.domain.model.user.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {

    public User toDomain(UserCreateDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .realName(dto.getRealName())
                .status(UserStatus.ENABLED)
                .build();
    }

    public User toDomain(Long id, UserUpdateDTO dto) {
        User user = User.builder()
                .id(id)
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .realName(dto.getRealName())
                .build();

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }

        if (dto.getStatus() != null) {
            user.setStatus(UserStatus.fromCode(dto.getStatus()));
        }

        return user;
    }

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .realName(user.getRealName())
                .status(user.getStatus() != null ? user.getStatus().getCode() : null)
                .statusDescription(user.getStatus() != null ? user.getStatus().getDescription() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}