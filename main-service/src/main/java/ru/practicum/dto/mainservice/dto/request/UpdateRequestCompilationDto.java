package ru.practicum.dto.mainservice.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateRequestCompilationDto {

    private List<Long> events;
    private boolean pinned;
    private String title;
}
