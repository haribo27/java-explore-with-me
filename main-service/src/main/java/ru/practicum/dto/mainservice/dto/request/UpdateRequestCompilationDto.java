package ru.practicum.dto.mainservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateRequestCompilationDto {

    private List<Long> events;
    private boolean pinned;
    @Size(min = 1, max = 50)
    private String title;
}
