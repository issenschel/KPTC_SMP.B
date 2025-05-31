package com.example.kptc_smp.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Модель получения информации о профиле пользователе")
public class UserProfileResponseDto {
    @Schema(description = "Имя пользователя", example = "Keecth_Krut")
    private String username;
    @Schema(description = "Список ролей пользователя", example = "ROLE_USER")
    private List<String> roles;
    @Schema(description = "Ссылка на аватарку", example = "localhost:5174/bambam")
    private String avatarUrl;
}
