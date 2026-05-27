package com.lookatbar.scp.basicdata.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String id;
    private String username;
    private String email;
    private String phone;
    private String realName;
    private Integer status;
    private String statusDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RoleDTO> roles;
}