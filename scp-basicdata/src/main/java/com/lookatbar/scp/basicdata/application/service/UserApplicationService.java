package com.lookatbar.scp.basicdata.application.service;

import com.lookatbar.scp.basicdata.application.assembler.PermissionAssembler;
import com.lookatbar.scp.basicdata.application.assembler.RoleAssembler;
import com.lookatbar.scp.basicdata.application.assembler.UserAssembler;
import com.lookatbar.scp.basicdata.application.dto.*;
import com.lookatbar.scp.basicdata.domain.model.user.User;
import com.lookatbar.scp.basicdata.domain.repository.UserRepository;
import com.lookatbar.scp.basicdata.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final UserAssembler userAssembler;
    private final RoleAssembler roleAssembler;
    private final PermissionAssembler permissionAssembler;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO createUser(UserCreateDTO dto) {
        User user = userAssembler.toDomain(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userDomainService.createUser(user);
        return userAssembler.toDTO(savedUser);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserUpdateDTO dto) {
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        User user = userAssembler.toDomain(id, dto);
        User updatedUser = userDomainService.updateUser(user);
        return userAssembler.toDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        userDomainService.deleteUser(id);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        UserDTO dto = userAssembler.toDTO(user);
        dto.setRoles(userDomainService.getUserRoles(id).stream()
                .map(roleAssembler::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDTO dto = userAssembler.toDTO(user);
                    dto.setRoles(userDomainService.getUserRoles(user.getId()).stream()
                            .map(roleAssembler::toDTO)
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void assignRoles(Long userId, AssignRoleDTO dto) {
        userDomainService.assignRoles(userId, dto.getRoleIds());
    }

    @Transactional
    public void revokeRole(Long userId, Long roleId) {
        userDomainService.revokeRole(userId, roleId);
    }

    public boolean hasPermission(Long userId, String permissionCode) {
        return userDomainService.hasPermission(userId, permissionCode);
    }
}