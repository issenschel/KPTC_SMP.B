package com.example.kptc_smp.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Модель получения информации о пользователе")
public class UserInformationDto {
    @Schema(description = "Идентификатор", example = "1")
    private Integer id;
    @Schema(description = "Имя пользователя", example = "Keecth_Krut")
    private String username;
    @Schema(description = "Почта", example = "doingbusiness@gmail.com")
    private String email;
}
