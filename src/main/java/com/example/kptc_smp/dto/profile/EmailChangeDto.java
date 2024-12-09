package com.example.kptc_smp.dto.profile;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmailChangeDto {
    @NotNull
    @Size(max = 6, message = "Код не может быть больше 6")
    private String code;
    @NotBlank(message = "Почта не может быть пустой")
    @Email(message = "Почта должна быть валидной")
    private String email;
}
