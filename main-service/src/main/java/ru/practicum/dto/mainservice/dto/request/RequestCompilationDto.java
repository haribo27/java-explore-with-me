package ru.practicum.dto.mainservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RequestCompilationDto {

    private List<Long> events;
    private boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
