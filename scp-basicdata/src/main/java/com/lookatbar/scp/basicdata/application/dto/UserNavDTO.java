package com.lookatbar.scp.basicdata.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户导航菜单响应DTO
 * 用于返回当前用户的菜单权限信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNavDTO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 用户角色列表
     */
    private List<String> roles;

    /**
     * 用户权限列表
     */
    private List<String> permissions;

    /**
     * 菜单列表
     */
    private List<MenuDTO> menus;

    /**
     * 菜单DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuDTO {
        /**
         * 菜单ID
         */
        private String id;

        /**
         * 权限编码
         */
        private String key;

        /**
         * 菜单路径
         */
        private String path;

        /**
         * 菜单名称
         */
        private String name;

        /**
         * 菜单标题
         */
        private String title;

        /**
         * 菜单图标
         */
        private String icon;

        /**
         * 父菜单ID
         */
        private String parentId;

        /**
         * 组件路径
         */
        private String component;

        /**
         * 是否隐藏
         */
        private Boolean hidden;

        /**
         * 子菜单
         */
        private List<MenuDTO> children;

        /**
         * 元信息
         */
        private Meta meta;

        /**
         * 元信息
         */
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Meta {
            /**
             * 标题
             */
            private String title;

            /**
             * 图标
             */
            private String icon;

            /**
             * 是否隐藏
             */
            private Boolean hidden;
        }
    }
}