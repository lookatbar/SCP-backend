package com.lookatbar.scp.basicdata.application.assembler;

import com.lookatbar.scp.basicdata.application.dto.UserCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.UserDTO;
import com.lookatbar.scp.basicdata.application.dto.UserUpdateDTO;
import com.lookatbar.scp.basicdata.domain.model.user.User;
import com.lookatbar.scp.basicdata.domain.model.user.UserStatus;
import org.springframework.stereotype.Component;

/**
 * 用户领域对象与DTO转换器
 * 负责User领域模型与UserDTO、UserCreateDTO、UserUpdateDTO之间的转换
 */
@Component
public class UserAssembler {

    /**
     * 将用户创建DTO转换为领域模型
     *
     * @param dto 用户创建请求DTO
     * @return 用户领域模型
     */
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

    /**
     * 将用户更新DTO转换为领域模型
     *
     * @param id  用户ID
     * @param dto 用户更新请求DTO
     * @return 用户领域模型
     */
    public User toDomain(String id, UserUpdateDTO dto) {
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

    /**
     * 将用户领域模型转换为DTO
     *
     * @param user 用户领域模型
     * @return 用户DTO
     */
    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .realName(user.getRealName())
                .status(user.getStatus() != null ? user.getStatus().getCode() : null)
                .statusDescription(user.getStatus() != null ? user.getStatus().getDescription() : null)
                .createdAt(user.getCreatedTime())
                .updatedAt(user.getModifiedTime())
                .build();
    }
}