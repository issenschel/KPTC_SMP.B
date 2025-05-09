package com.example.kptc_smp.dto.email;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Модель кода")
public class CodeDto {
    @Schema(description = "Код", example = "228422")
    @NotNull
    @Size(max = 6, message = "Код не может быть больше 6")
    @Pattern(regexp = "^[0-9]+$",
            message = "Код должен содержать только цифры")
    private String code;
}
