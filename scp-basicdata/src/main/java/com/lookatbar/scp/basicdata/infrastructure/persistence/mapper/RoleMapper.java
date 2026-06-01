package com.lookatbar.scp.basicdata.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.RolePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * 角色Mapper接口
 * 基于MyBatis Plus实现角色数据访问
 */
@Mapper
public interface RoleMapper extends BaseMapper<RolePO> {

    /**
     * 根据角色编码查询角色
     *
     * @param code 角色编码
     * @return 角色PO
     */
    @Select("SELECT * FROM basicdata_role WHERE code = #{code}")
    Optional<RolePO> findByCode(@Param("code") String code);

    /**
     * 根据角色名称查询角色
     *
     * @param name 角色名称
     * @return 角色PO
     */
    @Select("SELECT * FROM basicdata_role WHERE name = #{name}")
    Optional<RolePO> findByName(@Param("name") String name);

    /**
     * 根据状态查询角色列表
     *
     * @param status 状态
     * @return 角色PO列表
     */
    @Select("SELECT * FROM basicdata_role WHERE status = #{status}")
    List<RolePO> findByStatus(@Param("status") Integer status);

    /**
     * 根据用户ID查询用户拥有的角色列表
     *
     * @param userId 用户ID
     * @return 角色PO列表
     */
    @Select("SELECT r.* FROM basicdata_role r " +
            "JOIN basicdata_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<RolePO> findByUserId(@Param("userId") String userId);

    /**
     * 查询角色的父角色列表（角色继承）
     *
     * @param roleId 角色ID
     * @return 父角色PO列表
     */
    @Select("SELECT r.* FROM basicdata_role r " +
            "JOIN basicdata_role_hierarchy rh ON r.id = rh.parent_role_id " +
            "WHERE rh.child_role_id = #{roleId}")
    List<RolePO> findParentRoles(@Param("roleId") String roleId);

    /**
     * 查询角色的子角色列表（角色继承）
     *
     * @param roleId 角色ID
     * @return 子角色PO列表
     */
    @Select("SELECT r.* FROM basicdata_role r " +
            "JOIN basicdata_role_hierarchy rh ON r.id = rh.child_role_id " +
            "WHERE rh.parent_role_id = #{roleId}")
    List<RolePO> findChildRoles(@Param("roleId") String roleId);

    /**
     * 根据角色编码统计数量
     *
     * @param code 角色编码
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM basicdata_role WHERE code = #{code}")
    int countByCode(@Param("code") String code);

    /**
     * 根据角色名称统计数量
     *
     * @param name 角色名称
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM basicdata_role WHERE name = #{name}")
    int countByName(@Param("name") String name);

    /**
     * 条件过滤分页查询角色
     *
     * @param name   角色名称（模糊查询，可选）
     * @param code   角色编码（模糊查询，可选）
     * @param status 状态（精确查询，可选）
     * @param offset 偏移量
     * @param limit  每页条数
     * @return 角色PO列表
     */
    @Select("<script>" +
            "SELECT * FROM basicdata_role " +
            "WHERE 1=1 " +
            "<if test='name != null and name != \"\"'>" +
            "AND name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "<if test='code != null and code != \"\"'>" +
            "AND code LIKE CONCAT('%', #{code}, '%') " +
            "</if>" +
            "<if test='status != null and status != -1'>" +
            "AND status = #{status} " +
            "</if>" +
            "ORDER BY created_time DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<RolePO> findByCondition(@Param("name") String name,
                                  @Param("code") String code,
                                  @Param("status") Integer status,
                                  @Param("offset") Integer offset,
                                  @Param("limit") Integer limit);

    /**
     * 条件过滤统计角色数量
     *
     * @param name   角色名称（模糊查询，可选）
     * @param code   角色编码（模糊查询，可选）
     * @param status 状态（精确查询，可选）
     * @return 角色数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM basicdata_role " +
            "WHERE 1=1 " +
            "<if test='name != null and name != \"\"'>" +
            "AND name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "<if test='code != null and code != \"\"'>" +
            "AND code LIKE CONCAT('%', #{code}, '%') " +
            "</if>" +
            "<if test='status != null and status != -1'>" +
            "AND status = #{status} " +
            "</if>" +
            "</script>")
    long countByCondition(@Param("name") String name,
                          @Param("code") String code,
                          @Param("status") Integer status);
}