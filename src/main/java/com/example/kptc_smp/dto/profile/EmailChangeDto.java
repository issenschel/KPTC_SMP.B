package com.example.kptc_smp.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Модель смены почты")
public class EmailChangeDto {

    @Schema(description = "Код", example = "228422")
    @NotNull
    @Size(max = 6, message = "Код не может быть больше 6")
    @Pattern(regexp = "^[0-9]+$",
            message = "Код должен содержать только цифры")
    private String code;

    @Schema(description = "Почта", example = "muzhik@yandex.ru")
    @NotBlank(message = "Почта не может быть пустой")
    @Email(message = "Почта должна быть валидной")
    private String email;

    @Schema(description = "Тикет действия", example = "5125125.5")
    @NotBlank(message = "Тикет не может быть пустым")
    private String actionTicket;
}
