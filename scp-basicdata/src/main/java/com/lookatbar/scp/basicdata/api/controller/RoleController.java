package com.lookatbar.scp.basicdata.api.controller;

import com.lookatbar.scp.basicdata.application.dto.AssignPermissionDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleUpdateDTO;
import com.lookatbar.scp.basicdata.application.service.RoleApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleApplicationService roleApplicationService;

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleCreateDTO dto) {
        RoleDTO role = roleApplicationService.createRole(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable String id, @Valid @RequestBody RoleUpdateDTO dto) {
        RoleDTO role = roleApplicationService.updateRole(id, dto);
        return ResponseEntity.ok(role);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable String id) {
        roleApplicationService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable String id) {
        RoleDTO role = roleApplicationService.getRoleById(id);
        return ResponseEntity.ok(role);
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleApplicationService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<Void> assignPermissions(@PathVariable String id, @Valid @RequestBody AssignPermissionDTO dto) {
        roleApplicationService.assignPermissions(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Void> revokePermission(@PathVariable String roleId, @PathVariable String permissionId) {
        roleApplicationService.revokePermission(roleId, permissionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/inherit")
    public ResponseEntity<Void> inheritRole(@RequestParam String parentRoleId, @RequestParam String childRoleId) {
        roleApplicationService.inheritRole(parentRoleId, childRoleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/inherit")
    public ResponseEntity<Void> removeInheritance(@RequestParam String parentRoleId, @RequestParam String childRoleId) {
        roleApplicationService.removeInheritance(parentRoleId, childRoleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/permissions/check")
    public ResponseEntity<Boolean> checkPermission(@PathVariable String id, @RequestParam String permissionCode) {
        boolean hasPermission = roleApplicationService.hasPermission(id, permissionCode);
        return ResponseEntity.ok(hasPermission);
    }
}