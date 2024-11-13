package ru.practicum.dto.mainservice.dto.compilation;

import lombok.Data;
import ru.practicum.dto.mainservice.dto.event.EventShortDto;

import java.util.List;

@Data
public class CompilationDto {

    private List<EventShortDto> events;
    private long id;
    private boolean pinned;
    private String title;
}
