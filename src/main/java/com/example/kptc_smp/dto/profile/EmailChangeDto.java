package com.example.kptc_smp.dto.profile;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmailChangeDto {
    @NotNull
    @Size(max = 6, message = "Код не может быть больше 6")
    @Pattern(regexp = "^[0-9]+$",
            message = "Код должен содержать только цифры")
    private String code;
    @NotBlank(message = "Почта не может быть пустой")
    @Email(message = "Почта должна быть валидной")
    private String email;
}
