package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.PermissionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * 权限Mapper接口
 * 基于MyBatis Plus实现权限数据访问
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionPO> {

    /**
     * 根据权限编码查询权限
     *
     * @param code 权限编码
     * @return 权限PO
     */
    @Select("SELECT * FROM basicdata_permission WHERE code = #{code}")
    Optional<PermissionPO> findByCode(@Param("code") String code);

    /**
     * 根据状态查询权限列表
     *
     * @param status 状态
     * @return 权限PO列表
     */
    @Select("SELECT * FROM basicdata_permission WHERE status = #{status}")
    List<PermissionPO> findByStatus(@Param("status") Integer status);

    /**
     * 根据类型查询权限列表
     *
     * @param type 类型（1-菜单，2-按钮，3-API）
     * @return 权限PO列表
     */
    @Select("SELECT * FROM basicdata_permission WHERE type = #{type}")
    List<PermissionPO> findByType(@Param("type") Integer type);

    /**
     * 根据父权限ID查询子权限列表
     *
     * @param parentId 父权限ID
     * @return 子权限PO列表
     */
    @Select("SELECT * FROM basicdata_permission WHERE parent_id = #{parentId}")
    List<PermissionPO> findByParentId(@Param("parentId") String parentId);

    /**
     * 根据角色ID查询角色拥有的权限列表
     *
     * @param roleId 角色ID
     * @return 权限PO列表
     */
    @Select("SELECT p.* FROM basicdata_permission p " +
            "JOIN basicdata_role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    List<PermissionPO> findByRoleId(@Param("roleId") String roleId);

    /**
     * 根据用户ID查询用户拥有的所有权限（含继承权限）
     * 包含直接分配的权限和通过角色继承获得的权限
     *
     * @param userId 用户ID
     * @return 权限PO列表
     */
    @Select("SELECT p.* FROM basicdata_permission p " +
            "JOIN basicdata_role_permission rp ON p.id = rp.permission_id " +
            "JOIN basicdata_user_role ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} " +
            "UNION " +
            "SELECT p.* FROM basicdata_permission p " +
            "JOIN basicdata_role_permission rp ON p.id = rp.permission_id " +
            "JOIN basicdata_role_hierarchy rh ON rp.role_id = rh.parent_role_id " +
            "JOIN basicdata_user_role ur ON rh.child_role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<PermissionPO> findByUserId(@Param("userId") String userId);

    /**
     * 根据权限编码统计数量
     *
     * @param code 权限编码
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM basicdata_permission WHERE code = #{code}")
    int countByCode(@Param("code") String code);
}