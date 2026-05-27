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

/**
 * 用户领域服务
 * 处理用户相关的核心业务逻辑，包括用户创建、更新、删除、角色分配等
 */
@Service
@RequiredArgsConstructor
public class UserDomainService {

    /**
     * 用户仓储接口
     */
    private final UserRepository userRepository;

    /**
     * 角色仓储接口
     */
    private final RoleRepository roleRepository;

    /**
     * 权限仓储接口
     */
    private final PermissionRepository permissionRepository;

    /**
     * 创建用户
     * 
     * @param user 用户领域对象
     * @return 创建成功的用户对象
     * @throws IllegalArgumentException 当用户名、邮箱或手机号已存在时抛出
     */
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

    /**
     * 更新用户信息
     * 
     * @param user 用户领域对象
     * @return 更新后的用户对象
     * @throws IllegalArgumentException 当用户不存在或用户名/邮箱/手机号重复时抛出
     */
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

    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @throws IllegalArgumentException 当用户不存在时抛出
     */
    @Transactional
    public void deleteUser(String userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("用户不存在");
        }
        userRepository.deleteById(userId);
    }

    /**
     * 为用户分配单个角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @throws IllegalArgumentException 当用户或角色不存在时抛出
     */
    @Transactional
    public void assignRole(String userId, String roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在"));

        permissionRepository.addUserRole(userId, roleId);
    }

    /**
     * 撤销用户的指定角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    @Transactional
    public void revokeRole(String userId, String roleId) {
        permissionRepository.removeUserRole(userId, roleId);
    }

    /**
     * 为用户批量分配角色
     * 
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     */
    @Transactional
    public void assignRoles(String userId, List<String> roleIds) {
        roleIds.forEach(roleId -> assignRole(userId, roleId));
    }

    /**
     * 检查用户是否具有指定权限
     * 
     * @param userId         用户ID
     * @param permissionCode 权限编码
     * @return 是否具有该权限
     */
    public boolean hasPermission(String userId, String permissionCode) {
        return permissionRepository.findByUserId(userId).stream()
                .anyMatch(permission -> permission.getCode().equals(permissionCode));
    }

    /**
     * 获取用户的角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<Role> getUserRoles(String userId) {
        return roleRepository.findByUserId(userId);
    }
}