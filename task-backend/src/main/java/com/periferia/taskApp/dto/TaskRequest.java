package com.periferia.taskApp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {

    @NotNull(message = "El titulo es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String title;
    @Size(max = 150, message = "La descripción no puede tener más de 150 caracteres")
    private String description;
    @NotNull(message = "El estado es obligatorio")
    @Pattern(regexp = "PENDING|IN_PROGRESS|DONE",
            message = "Estado no válido. Valores permitidos: PENDING, IN_PROGRESS, DONE")
    private String status;
}
