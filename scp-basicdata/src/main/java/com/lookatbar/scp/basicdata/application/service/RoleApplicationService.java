package com.lookatbar.scp.basicdata.application.service;

import com.lookatbar.scp.basicdata.application.assembler.PermissionAssembler;
import com.lookatbar.scp.basicdata.application.assembler.RoleAssembler;
import com.lookatbar.scp.basicdata.application.dto.AssignPermissionDTO;
import com.lookatbar.scp.basicdata.application.dto.PageResponseDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleQueryDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleUpdateDTO;
import com.lookatbar.scp.basicdata.domain.model.role.Role;
import com.lookatbar.scp.basicdata.domain.repository.RoleQueryCondition;
import com.lookatbar.scp.basicdata.domain.repository.RoleRepository;
import com.lookatbar.scp.basicdata.domain.service.AuditService;
import com.lookatbar.scp.basicdata.domain.service.RoleDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色应用服务
 * 负责角色管理的应用层业务编排，协调领域服务和数据访问
 */
@Service
@RequiredArgsConstructor
public class RoleApplicationService {

    /**
     * 角色领域服务，处理核心业务逻辑
     */
    private final RoleDomainService roleDomainService;

    /**
     * 角色仓储接口，提供数据访问能力
     */
    private final RoleRepository roleRepository;

    /**
     * 角色领域对象与DTO的转换器
     */
    private final RoleAssembler roleAssembler;

    /**
     * 权限领域对象与DTO的转换器
     */
    private final PermissionAssembler permissionAssembler;

    /**
     * 审计字段处理服务
     */
    private final AuditService auditService;

    /**
     * 创建角色
     * 
     * @param dto 角色创建请求DTO
     * @return 创建成功的角色DTO
     */
    @Transactional
    public RoleDTO createRole(RoleCreateDTO dto) {
        Role role = roleAssembler.toDomain(dto);
        auditService.setCreateInfo(role, dto.getCreatedBy());
        Role savedRole = roleDomainService.createRole(role);
        return roleAssembler.toDTO(savedRole);
    }

    /**
     * 更新角色信息
     * 
     * @param id  角色ID
     * @param dto 角色更新请求DTO
     * @return 更新后的角色DTO
     */
    @Transactional
    public RoleDTO updateRole(String id, RoleUpdateDTO dto) {
        Role role = roleAssembler.toDomain(id, dto);
        auditService.setUpdateInfo(role, dto.getModifiedBy());
        Role updatedRole = roleDomainService.updateRole(role);
        return roleAssembler.toDTO(updatedRole);
    }

    /**
     * 删除角色
     * 
     * @param id 角色ID
     */
    @Transactional
    public void deleteRole(String id) {
        roleDomainService.deleteRole(id);
    }

    /**
     * 根据ID查询角色详情
     * 
     * @param id 角色ID
     * @return 角色DTO，包含权限列表和继承关系
     */
    public RoleDTO getRoleById(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在"));
        RoleDTO dto = roleAssembler.toDTO(role);
        dto.setPermissions(roleDomainService.getRolePermissions(id).stream()
                .map(permissionAssembler::toDTO)
                .collect(Collectors.toList()));
        dto.setParentRoles(roleRepository.findParentRoles(id).stream()
                .map(roleAssembler::toDTO)
                .collect(Collectors.toList()));
        dto.setChildRoles(roleRepository.findChildRoles(id).stream()
                .map(roleAssembler::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    /**
     * 查询所有角色列表
     * 
     * @return 角色DTO列表，包含权限信息
     */
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> {
                    RoleDTO dto = roleAssembler.toDTO(role);
                    dto.setPermissions(roleDomainService.getRolePermissions(role.getId()).stream()
                            .map(permissionAssembler::toDTO)
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 为角色分配权限
     * 
     * @param roleId 角色ID
     * @param dto    权限分配请求DTO
     */
    @Transactional
    public void assignPermissions(String roleId, AssignPermissionDTO dto) {
        roleDomainService.assignPermissions(roleId, dto.getPermissionIds());
    }

    /**
     * 撤销角色的指定权限
     * 
     * @param roleId       角色ID
     * @param permissionId 权限ID
     */
    @Transactional
    public void revokePermission(String roleId, String permissionId) {
        roleDomainService.revokePermission(roleId, permissionId);
    }

    /**
     * 设置角色继承关系
     * 
     * @param parentRoleId 父角色ID
     * @param childRoleId  子角色ID
     */
    @Transactional
    public void inheritRole(String parentRoleId, String childRoleId) {
        roleDomainService.inheritRole(parentRoleId, childRoleId);
    }

    /**
     * 移除角色继承关系
     * 
     * @param parentRoleId 父角色ID
     * @param childRoleId  子角色ID
     */
    @Transactional
    public void removeInheritance(String parentRoleId, String childRoleId) {
        roleDomainService.removeInheritance(parentRoleId, childRoleId);
    }

    /**
     * 检查角色是否具有指定权限（含继承权限）
     * 
     * @param roleId         角色ID
     * @param permissionCode 权限编码
     * @return 是否具有该权限
     */
    public boolean hasPermission(String roleId, String permissionCode) {
        return roleDomainService.hasPermission(roleId, permissionCode);
    }

    /**
     * 条件过滤分页查询角色
     * 
     * @param queryDTO 查询条件DTO
     * @return 分页响应DTO
     */
    public PageResponseDTO<RoleDTO> queryRoles(RoleQueryDTO queryDTO) {
        // 设置默认分页参数
        int page = queryDTO.getPage() != null && queryDTO.getPage() > 0 ? queryDTO.getPage() : 1;
        int pageSize = queryDTO.getPageSize() != null && queryDTO.getPageSize() > 0 ? queryDTO.getPageSize() : 10;
        int offset = (page - 1) * pageSize;

        // 构建查询条件
        RoleQueryCondition condition = new RoleQueryCondition(
                queryDTO.getName(),
                queryDTO.getCode(),
                queryDTO.getStatus(),
                offset,
                pageSize
        );

        // 查询数据
        List<Role> roles = roleRepository.findByCondition(condition);
        long total = roleRepository.countByCondition(condition);

        // 转换为DTO并填充权限信息
        List<RoleDTO> roleDTOs = roles.stream()
                .map(role -> {
                    RoleDTO dto = roleAssembler.toDTO(role);
                    dto.setPermissions(roleDomainService.getRolePermissions(role.getId()).stream()
                            .map(permissionAssembler::toDTO)
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());

        return PageResponseDTO.of(roleDTOs, total, page, pageSize);
    }
}