package com.lookatbar.scp.basicdata.domain.service;

import com.lookatbar.scp.basicdata.domain.model.role.Role;
import com.lookatbar.scp.basicdata.domain.model.user.User;
import com.lookatbar.scp.basicdata.domain.repository.PermissionRepository;
import com.lookatbar.scp.basicdata.domain.repository.RoleRepository;
import com.lookatbar.scp.basicdata.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("邮箱已存在");
        }
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new IllegalArgumentException("手机号已存在");
        }
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(User user) {
        User existing = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        if (!existing.getUsername().equals(user.getUsername()) 
                && userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (user.getEmail() != null && !existing.getEmail().equals(user.getEmail()) 
                && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("邮箱已存在");
        }
        if (user.getPhone() != null && !existing.getPhone().equals(user.getPhone()) 
                && userRepository.existsByPhone(user.getPhone())) {
            throw new IllegalArgumentException("手机号已存在");
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("用户不存在");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void assignRole(String userId, String roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在"));

        permissionRepository.addUserRole(userId, roleId);
    }

    @Transactional
    public void revokeRole(String userId, String roleId) {
        permissionRepository.removeUserRole(userId, roleId);
    }

    @Transactional
    public void assignRoles(String userId, List<String> roleIds) {
        roleIds.forEach(roleId -> assignRole(userId, roleId));
    }

    public boolean hasPermission(String userId, String permissionCode) {
        return permissionRepository.findByUserId(userId).stream()
                .anyMatch(permission -> permission.getCode().equals(permissionCode));
    }

    public List<Role> getUserRoles(String userId) {
        return roleRepository.findByUserId(userId);
    }
}