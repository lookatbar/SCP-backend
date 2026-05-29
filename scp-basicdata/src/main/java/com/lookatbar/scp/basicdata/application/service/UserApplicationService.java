package com.lookatbar.scp.basicdata.application.service;

import cn.hutool.core.collection.CollectionUtil;
import com.lookatbar.scp.basicdata.application.assembler.PermissionAssembler;
import com.lookatbar.scp.basicdata.application.assembler.RoleAssembler;
import com.lookatbar.scp.basicdata.application.assembler.UserAssembler;
import com.lookatbar.scp.basicdata.application.dto.*;
import com.lookatbar.scp.basicdata.domain.model.permission.Permission;
import com.lookatbar.scp.basicdata.domain.model.role.Role;
import com.lookatbar.scp.basicdata.domain.model.role.RoleCode;
import com.lookatbar.scp.basicdata.domain.model.user.User;
import com.lookatbar.scp.basicdata.domain.repository.UserRepository;
import com.lookatbar.scp.basicdata.domain.service.UserDomainService;
import com.lookatbar.scp.basicdata.infrastructure.context.UserContext;
import com.lookatbar.scp.basicdata.infrastructure.context.UserInfo;
import com.lookatbar.scp.basicdata.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
                .map(Role::getCode).collect(Collectors.toList()));
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
                            .map(Role::getCode).collect(Collectors.toList()));
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
     * 检查用户是否具有指定角色
     *
     * @param userId   用户ID
     * @param roleCode 角色编码
     * @return 是否具有该角色
     */
    public boolean hasRole(String userId, String roleCode) {
        return userDomainService.hasRole(userId, roleCode);
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
        List<Permission> permissions = roleCodes.contains(RoleCode.SUPER_ADMIN.getCode())
                ? userDomainService.getAllPermissions()
                : userDomainService.getUserPermissions(user.getId());
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

    /**
     * 获取当前用户信息（从JWT token解析用户ID）
     *
     * @return 当前用户信息DTO
     */
    public UserDTO getCurrentUserInfo() {
        String userId = UserContext.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 获取用户角色列表
        List<String> roleCodes = userDomainService.getUserRoles(userId).stream()
                .map(Role::getCode)
                .collect(Collectors.toList());

        // 获取用户权限列表
        List<String> permissionCodes = (RoleCode.isSuperAdmin(roleCodes)
                ? userDomainService.getAllPermissions()
                : userDomainService.getUserPermissions(userId))
                .stream()
                .map(Permission::getCode)
                .collect(Collectors.toList());

        UserDTO dto = UserDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus().getCode())
                .roles(roleCodes)  // 字符串数组格式，前端期望
                .permissions(permissionCodes)  // 权限列表
                .build();

        return dto;
    }

    /**
     * 获取当前用户的导航菜单
     *
     * @return 用户导航DTO，包含用户信息和菜单列表
     */
    public UserNavDTO getCurrentUserNav() {
        UserInfo user = UserContext.getUserInfo();

        /**
         * 获取用户角色权限
         */
        List<Permission> permissions = user.isSuperAdmin()
                ? userDomainService.getAllPermissions()
                : userDomainService.getUserPermissions(user.getUserId());

        // 获取菜单类型的权限（构建菜单树）
        List<UserNavDTO.MenuDTO> menus = buildMenuTree(permissions);

        return UserNavDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .roles(user.getRoles())
                .permissions(user.getPermissions())
                .menus(menus)
                .build();
    }

    /**
     * 根据权限列表构建菜单树
     *
     * @param permissions 权限列表（扁平结构）
     * @return 菜单树（树形结构）
     */
    private List<UserNavDTO.MenuDTO> buildMenuTree(List<Permission> permissions) {
        // 过滤出菜单类型的权限（type=1 菜单，type=2 页面/按钮，排除 type=3 API权限）
        List<Permission> menuPermissions = permissions.stream()
                .filter(p -> p.getType() != null && p.getType().getCode() != 3)
                .collect(Collectors.toList());

        // 构建权限ID到权限对象的映射
        Map<String, Permission> permissionMap = menuPermissions.stream()
                .collect(Collectors.toMap(Permission::getId, p -> p));

        // 构建树形结构：找到顶级权限，并为每个权限设置子权限
        List<Permission> rootPermissions = CollectionUtil.newArrayList();
        for (Permission permission : menuPermissions) {
            String parentId = permission.getParentId();
            if (parentId == null || parentId.isEmpty()) {
                // 顶级权限
                rootPermissions.add(permission);
            } else {
                // 子权限，添加到父权限的children列表
                Permission parent = permissionMap.get(parentId);
                if (parent != null) {
                    parent.addChild(permission);
                }
            }
        }

        // 按排序号排序
        rootPermissions.sort(Comparator.comparing(Permission::getSortOrder, Comparator.nullsLast(Integer::compareTo)));

        // 转换为菜单DTO
        return rootPermissions.stream()
                .map(this::convertToMenuDTO)
                .collect(Collectors.toList());
    }

    /**
     * 将权限转换为菜单DTO
     *
     * @param permission 权限
     * @return 菜单DTO
     */
    private UserNavDTO.MenuDTO convertToMenuDTO(Permission permission) {
        UserNavDTO.MenuDTO.MenuDTOBuilder builder = UserNavDTO.MenuDTO.builder()
                .id(permission.getId())
                .key(permission.getCode())
                .path(permission.getPath() != null ? permission.getPath() : "/")
                .title(permission.getName())
                .icon(permission.getDescription()) // 暂时用description作为icon
                .parentId(permission.getParentId())
                .hidden(false);

        // 如果有子权限，递归转换
        if (permission.getChildren() != null && !permission.getChildren().isEmpty()) {
            List<UserNavDTO.MenuDTO> children = permission.getChildren().stream()
                    .map(this::convertToMenuDTO)
                    .collect(Collectors.toList());
            builder.children(children);
        }

        return builder.build();
    }
}