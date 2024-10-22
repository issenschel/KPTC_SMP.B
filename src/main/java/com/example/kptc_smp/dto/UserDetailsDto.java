package com.example.kptc_smp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailsDto {
    private Long id;
    private String username;
    private String email;
    private String minecraftName;
}
