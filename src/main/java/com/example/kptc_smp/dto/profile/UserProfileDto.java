package com.example.kptc_smp.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Модель получения информации о профиле пользователе")
public class UserProfileDto {
    @Schema(description = "Имя пользователя", example = "Keecth_Krut")
    private String username;
    @Schema(description = "Ссылка на аватарку", example = "localhost:5174/bambam")
    private String imageUrl;
}
