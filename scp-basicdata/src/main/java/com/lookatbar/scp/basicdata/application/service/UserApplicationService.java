package com.lookatbar.scp.basicdata.application.service;

import com.lookatbar.scp.basicdata.application.assembler.PermissionAssembler;
import com.lookatbar.scp.basicdata.application.assembler.RoleAssembler;
import com.lookatbar.scp.basicdata.application.assembler.UserAssembler;
import com.lookatbar.scp.basicdata.application.dto.*;
import com.lookatbar.scp.basicdata.domain.model.permission.Permission;
import com.lookatbar.scp.basicdata.domain.model.role.Role;
import com.lookatbar.scp.basicdata.domain.model.user.User;
import com.lookatbar.scp.basicdata.domain.repository.UserRepository;
import com.lookatbar.scp.basicdata.domain.service.UserDomainService;
import com.lookatbar.scp.basicdata.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户应用服务
 * 负责用户管理的应用层业务编排，协调领域服务和数据访问
 */
@Service
@RequiredArgsConstructor
public class UserApplicationService {

    /**
     * 用户领域服务，处理核心业务逻辑
     */
    private final UserDomainService userDomainService;

    /**
     * 用户仓储接口，提供数据访问能力
     */
    private final UserRepository userRepository;

    /**
     * 用户领域对象与DTO的转换器
     */
    private final UserAssembler userAssembler;

    /**
     * 角色领域对象与DTO的转换器
     */
    private final RoleAssembler roleAssembler;

    /**
     * 权限领域对象与DTO的转换器
     */
    private final PermissionAssembler permissionAssembler;

    /**
     * 密码编码器，用于密码加密
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * JWT工具类，用于生成和解析JWT令牌
     */
    private final JwtUtil jwtUtil;

    /**
     * 创建用户
     * 
     * @param dto 用户创建请求DTO
     * @return 创建成功的用户DTO
     */
    @Transactional
    public UserDTO createUser(UserCreateDTO dto) {
        User user = userAssembler.toDomain(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userDomainService.createUser(user);
        return userAssembler.toDTO(savedUser);
    }

    /**
     * 更新用户信息
     * 
     * @param id  用户ID
     * @param dto 用户更新请求DTO
     * @return 更新后的用户DTO
     */
    @Transactional
    public UserDTO updateUser(String id, UserUpdateDTO dto) {
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        User user = userAssembler.toDomain(id, dto);
        User updatedUser = userDomainService.updateUser(user);
        return userAssembler.toDTO(updatedUser);
    }

    /**
     * 删除用户
     * 
     * @param id 用户ID
     */
    @Transactional
    public void deleteUser(String id) {
        userDomainService.deleteUser(id);
    }

    /**
     * 根据ID查询用户详情
     * 
     * @param id 用户ID
     * @return 用户DTO，包含角色信息
     */
    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        UserDTO dto = userAssembler.toDTO(user);
        dto.setRoles(userDomainService.getUserRoles(id).stream()
                .map(roleAssembler::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    /**
     * 查询所有用户列表
     * 
     * @return 用户DTO列表，包含角色信息
     */
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

    /**
     * 为用户分配角色
     * 
     * @param userId 用户ID
     * @param dto    角色分配请求DTO
     */
    @Transactional
    public void assignRoles(String userId, AssignRoleDTO dto) {
        userDomainService.assignRoles(userId, dto.getRoleIds());
    }

    /**
     * 撤销用户的指定角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    @Transactional
    public void revokeRole(String userId, String roleId) {
        userDomainService.revokeRole(userId, roleId);
    }

    /**
     * 检查用户是否具有指定权限
     * 
     * @param userId         用户ID
     * @param permissionCode 权限编码
     * @return 是否具有该权限
     */
    public boolean hasPermission(String userId, String permissionCode) {
        return userDomainService.hasPermission(userId, permissionCode);
    }

    /**
     * 用户登录
     * 
     * @param dto 登录请求DTO，包含用户名和密码
     * @return 登录响应DTO，包含用户信息和JWT令牌
     */
    public LoginResponseDTO login(LoginDTO dto) {
        // 根据用户名查询用户
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("用户名或密码错误"));
        
        // 验证密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        
        // 检查用户状态
        if (!user.isEnabled()) {
            throw new IllegalArgumentException("用户已被禁用");
        }
        
        // 获取用户角色列表
        List<Role> roles = userDomainService.getUserRoles(user.getId());
        List<String> roleCodes = roles.stream()
                .map(Role::getCode)
                .collect(Collectors.toList());
        
        // 获取用户权限列表
        List<Permission> permissions = userDomainService.getUserPermissions(user.getId());
        List<String> permissionCodes = permissions.stream()
                .map(Permission::getCode)
                .collect(Collectors.toList());
        
        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 构建登录响应
        return LoginResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getExpireSeconds())
                .expiresAt(jwtUtil.getExpirationTime(token))
                .roles(roleCodes)
                .permissions(permissionCodes)
                .build();
    }
}