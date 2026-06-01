package com.lookatbar.scp.basicdata.infrastructure.persistence.repository;

import com.lookatbar.scp.basicdata.domain.model.role.Role;
import com.lookatbar.scp.basicdata.domain.model.role.RoleStatus;
import com.lookatbar.scp.basicdata.domain.repository.RoleQueryCondition;
import com.lookatbar.scp.basicdata.domain.repository.RoleRepository;
import com.lookatbar.scp.basicdata.infrastructure.persistence.mapper.RoleHierarchyMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.mapper.RoleMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.RolePO;
import com.lookatbar.scp.basicdata.infrastructure.util.BeanConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色仓储实现类
 * 基于MyBatis Plus实现角色数据访问，包括角色继承关系管理
 * 使用 BeanConverter 简化对象转换逻辑
 */
@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    /**
     * 角色数据访问Mapper
     */
    private final RoleMapper roleMapper;

    /**
     * 角色继承关系Mapper
     */
    private final RoleHierarchyMapper roleHierarchyMapper;

    /**
     * 保存角色实体
     *
     * @param role 角色实体
     * @return 保存后的角色实体
     */
    @Override
    public Role save(Role role) {
        RolePO po = toPO(role);
        if (role.getId() == null) {
            roleMapper.insert(po);
        } else {
            roleMapper.updateById(po);
        }
        return toDomain(po);
    }

    /**
     * 根据ID查询角色
     *
     * @param id 角色ID
     * @return 角色实体（可选）
     */
    @Override
    public Optional<Role> findById(String id) {
        return Optional.ofNullable(this.toDomain(roleMapper.selectById(id)));
    }

    /**
     * 根据编码查询角色
     *
     * @param code 角色编码
     * @return 角色实体（可选）
     */
    @Override
    public Optional<Role> findByCode(String code) {
        return roleMapper.findByCode(code).map(this::toDomain);
    }

    /**
     * 根据名称查询角色
     *
     * @param name 角色名称
     * @return 角色实体（可选）
     */
    @Override
    public Optional<Role> findByName(String name) {
        return roleMapper.findByName(name).map(this::toDomain);
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<Role> findAll() {
        return roleMapper.selectList(null).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 根据状态查询角色
     *
     * @param status 角色状态
     * @return 角色列表
     */
    @Override
    public List<Role> findByStatus(Integer status) {
        return roleMapper.findByStatus(status).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 根据用户ID查询用户关联的角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<Role> findByUserId(String userId) {
        return roleMapper.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 查询角色的父角色列表（角色继承）
     *
     * @param roleId 角色ID
     * @return 父角色列表
     */
    @Override
    public List<Role> findParentRoles(String roleId) {
        return roleMapper.findParentRoles(roleId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 查询角色的子角色列表（角色继承）
     *
     * @param roleId 角色ID
     * @return 子角色列表
     */
    @Override
    public List<Role> findChildRoles(String roleId) {
        return roleMapper.findChildRoles(roleId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID删除角色
     *
     * @param id 角色ID
     */
    @Override
    public void deleteById(String id) {
        roleMapper.deleteById(id);
    }

    /**
     * 检查角色编码是否已存在
     *
     * @param code 角色编码
     * @return 是否存在
     */
    @Override
    public boolean existsByCode(String code) {
        return roleMapper.countByCode(code) > 0;
    }

    /**
     * 检查角色名称是否已存在
     *
     * @param name 角色名称
     * @return 是否存在
     */
    @Override
    public boolean existsByName(String name) {
        return roleMapper.countByName(name) > 0;
    }

    /**
     * 添加角色继承关系
     *
     * @param parentRoleId 父角色ID
     * @param childRoleId  子角色ID
     */
    @Override
    public void addRoleHierarchy(String parentRoleId, String childRoleId) {
        roleHierarchyMapper.insertRoleHierarchy(parentRoleId, childRoleId);
    }

    /**
     * 移除角色继承关系
     *
     * @param parentRoleId 父角色ID
     * @param childRoleId  子角色ID
     */
    @Override
    public void removeRoleHierarchy(String parentRoleId, String childRoleId) {
        roleHierarchyMapper.deleteRoleHierarchy(parentRoleId, childRoleId);
    }

    /**
     * 条件过滤分页查询角色
     *
     * @param condition 查询条件
     * @return 角色列表
     */
    @Override
    public List<Role> findByCondition(RoleQueryCondition condition) {
        return roleMapper.findByCondition(
                condition.getName(),
                condition.getCode(),
                condition.getStatus(),
                condition.getOffset(),
                condition.getLimit()
        ).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 条件过滤统计角色数量
     *
     * @param condition 查询条件
     * @return 角色数量
     */
    @Override
    public long countByCondition(RoleQueryCondition condition) {
        return roleMapper.countByCondition(
                condition.getName(),
                condition.getCode(),
                condition.getStatus()
        );
    }

    /**
     * 将领域模型转换为持久化对象
     * 使用 BeanConverter 进行属性复制，简化代码
     *
     * @param role 角色领域模型
     * @return 角色持久化对象
     */
    private RolePO toPO(Role role) {
        RolePO po = BeanConverter.convert(role, RolePO.class);
        // 处理枚举类型转换
        if (role.getStatus() != null) {
            po.setStatus(role.getStatus().getCode());
        }
        return po;
    }

    /**
     * 将持久化对象转换为领域模型
     * 使用 BeanConverter 进行属性复制，简化代码
     *
     * @param po 角色持久化对象
     * @return 角色领域模型
     */
    private Role toDomain(RolePO po) {
        if (po == null) {
            return null;
        }
        Role role = BeanConverter.convert(po, Role.class);
        // 处理枚举类型转换
        if (po.getStatus() != null) {
            role.setStatus(RoleStatus.fromCode(po.getStatus()));
        }
        return role;
    }
}
