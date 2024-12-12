package com.example.kptc_smp.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInformationDto {
    private Integer id;
    private String username;
    private String email;
}
