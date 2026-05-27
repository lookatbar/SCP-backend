package com.lookatbar.scp.basicdata.domain.model.user;

import com.lookatbar.scp.basicdata.domain.model.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String realName;
    private UserStatus status;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private LocalDateTime updatedTime;

    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    public void addRole(Role role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    public void removeRole(Role role) {
        if (roles != null) {
            roles.remove(role);
        }
    }

    public boolean hasRole(String roleCode) {
        if (roles == null) {
            return false;
        }
        return roles.stream()
                .anyMatch(role -> role.getCode().equals(roleCode));
    }

    public boolean hasPermission(String permissionCode) {
        if (roles == null) {
            return false;
        }
        return roles.stream()
                .anyMatch(role -> role.hasPermission(permissionCode));
    }

    public void enable() {
        this.status = UserStatus.ENABLED;
    }

    public void disable() {
        this.status = UserStatus.DISABLED;
    }
}