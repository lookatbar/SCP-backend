package com.lookatbar.scp.basicdata.application.service;

import com.lookatbar.scp.basicdata.application.assembler.PermissionAssembler;
import com.lookatbar.scp.basicdata.application.dto.PermissionCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionUpdateDTO;
import com.lookatbar.scp.basicdata.domain.model.permission.Permission;
import com.lookatbar.scp.basicdata.domain.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限应用服务
 * 负责权限管理的应用层业务编排，协调领域服务和数据访问
 */
@Service
@RequiredArgsConstructor
public class PermissionApplicationService {

    /**
     * 权限仓储接口，提供数据访问能力
     */
    private final PermissionRepository permissionRepository;

    /**
     * 权限领域对象与DTO的转换器
     */
    private final PermissionAssembler permissionAssembler;

    /**
     * 创建权限
     * 
     * @param dto 权限创建请求DTO
     * @return 创建成功的权限DTO
     */
    @Transactional
    public PermissionDTO createPermission(PermissionCreateDTO dto) {
        Permission permission = permissionAssembler.toDomain(dto);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionAssembler.toDTO(savedPermission);
    }

    /**
     * 更新权限信息
     * 
     * @param id  权限ID
     * @param dto 权限更新请求DTO
     * @return 更新后的权限DTO
     */
    @Transactional
    public PermissionDTO updatePermission(String id, PermissionUpdateDTO dto) {
        Permission permission = permissionAssembler.toDomain(id, dto);
        Permission updatedPermission = permissionRepository.save(permission);
        return permissionAssembler.toDTO(updatedPermission);
    }

    /**
     * 删除权限
     * 
     * @param id 权限ID
     */
    @Transactional
    public void deletePermission(String id) {
        permissionRepository.deleteById(id);
    }

    /**
     * 根据ID查询权限详情
     * 
     * @param id 权限ID
     * @return 权限DTO
     */
    public PermissionDTO getPermissionById(String id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("权限不存在"));
        return permissionAssembler.toDTO(permission);
    }

    /**
     * 查询所有权限列表
     * 
     * @return 权限DTO列表
     */
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionAssembler::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 按权限类型查询权限列表
     * 
     * @param type 权限类型：1-菜单权限，2-按钮权限，3-数据权限
     * @return 权限DTO列表
     */
    public List<PermissionDTO> getPermissionsByType(Integer type) {
        return permissionRepository.findByType(type).stream()
                .map(permissionAssembler::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 查询角色拥有的权限列表（含继承权限）
     * 
     * @param roleId 角色ID
     * @return 权限DTO列表
     */
    public List<PermissionDTO> getPermissionsByRoleId(String roleId) {
        return permissionRepository.findByRoleId(roleId).stream()
                .map(permissionAssembler::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 查询用户拥有的权限列表（含角色继承权限）
     * 
     * @param userId 用户ID
     * @return 权限DTO列表
     */
    public List<PermissionDTO> getPermissionsByUserId(String userId) {
        return permissionRepository.findByUserId(userId).stream()
                .map(permissionAssembler::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 获取权限树形结构
     * 
     * @return 权限树形结构列表
     */
    public List<PermissionDTO> getPermissionTree() {
        List<Permission> allPermissions = permissionRepository.findAll();
        List<Permission> rootPermissions = allPermissions.stream()
                .filter(p -> p.getParentId() == null || p.getParentId().isEmpty())
                .collect(Collectors.toList());

        return buildPermissionTree(rootPermissions, allPermissions);
    }

    /**
     * 递归构建权限树形结构
     * 
     * @param parents       父权限列表
     * @param allPermissions 所有权限列表
     * @return 权限树形结构DTO列表
     */
    private List<PermissionDTO> buildPermissionTree(List<Permission> parents, List<Permission> allPermissions) {
        List<PermissionDTO> result = new ArrayList<>();

        for (Permission parent : parents) {
            PermissionDTO dto = permissionAssembler.toDTO(parent);
            List<Permission> children = allPermissions.stream()
                    .filter(p -> p.getParentId() != null && p.getParentId().equals(parent.getId()))
                    .collect(Collectors.toList());

            if (!children.isEmpty()) {
                dto.setChildren(buildPermissionTree(children, allPermissions));
            }

            result.add(dto);
        }

        return result;
    }
}