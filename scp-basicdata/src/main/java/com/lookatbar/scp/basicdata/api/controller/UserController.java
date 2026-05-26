package com.lookatbar.scp.basicdata.api.controller;

import com.lookatbar.scp.basicdata.application.dto.AssignRoleDTO;
import com.lookatbar.scp.basicdata.application.dto.UserCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.UserDTO;
import com.lookatbar.scp.basicdata.application.dto.UserUpdateDTO;
import com.lookatbar.scp.basicdata.application.service.UserApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserApplicationService userApplicationService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO dto) {
        UserDTO user = userApplicationService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        UserDTO user = userApplicationService.updateUser(id, dto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userApplicationService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userApplicationService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userApplicationService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<Void> assignRoles(@PathVariable Long id, @Valid @RequestBody AssignRoleDTO dto) {
        userApplicationService.assignRoles(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> revokeRole(@PathVariable Long userId, @PathVariable Long roleId) {
        userApplicationService.revokeRole(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/permissions/check")
    public ResponseEntity<Boolean> checkPermission(@PathVariable Long id, @RequestParam String permissionCode) {
        boolean hasPermission = userApplicationService.hasPermission(id, permissionCode);
        return ResponseEntity.ok(hasPermission);
    }
}