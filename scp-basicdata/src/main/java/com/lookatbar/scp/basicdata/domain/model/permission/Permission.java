package com.lookatbar.scp.basicdata.domain.model.permission;

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
public class Permission {

    private Long id;
    private String name;
    private String code;
    private PermissionType type;
    private Long parentId;
    private String path;
    private String method;
    private String description;
    private Integer sortOrder;
    private PermissionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<Permission> children = new ArrayList<>();

    public void addChild(Permission child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        if (!children.contains(child)) {
            children.add(child);
        }
    }

    public void enable() {
        this.status = PermissionStatus.ENABLED;
    }

    public void disable() {
        this.status = PermissionStatus.DISABLED;
    }
}