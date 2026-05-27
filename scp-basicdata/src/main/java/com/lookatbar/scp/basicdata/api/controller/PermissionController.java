package com.lookatbar.scp.basicdata.api.controller;

import com.lookatbar.scp.basicdata.application.dto.PermissionCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionUpdateDTO;
import com.lookatbar.scp.basicdata.application.service.PermissionApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionApplicationService permissionApplicationService;

    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionCreateDTO dto) {
        PermissionDTO permission = permissionApplicationService.createPermission(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(permission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionDTO> updatePermission(@PathVariable String id, @Valid @RequestBody PermissionUpdateDTO dto) {
        PermissionDTO permission = permissionApplicationService.updatePermission(id, dto);
        return ResponseEntity.ok(permission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable String id) {
        permissionApplicationService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable String id) {
        PermissionDTO permission = permissionApplicationService.getPermissionById(id);
        return ResponseEntity.ok(permission);
    }

    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        List<PermissionDTO> permissions = permissionApplicationService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByType(@PathVariable Integer type) {
        List<PermissionDTO> permissions = permissionApplicationService.getPermissionsByType(type);
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByRoleId(@PathVariable String roleId) {
        List<PermissionDTO> permissions = permissionApplicationService.getPermissionsByRoleId(roleId);
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByUserId(@PathVariable String userId) {
        List<PermissionDTO> permissions = permissionApplicationService.getPermissionsByUserId(userId);
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/tree")
    public ResponseEntity<List<PermissionDTO>> getPermissionTree() {
        List<PermissionDTO> tree = permissionApplicationService.getPermissionTree();
        return ResponseEntity.ok(tree);
    }
}