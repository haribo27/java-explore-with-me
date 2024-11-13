package ru.practicum.dto.mainservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateRequestCompilationDto {

    private Set<Long> events;
    private boolean pinned;
    @Size(min = 1, max = 50)
    private String title;
}
