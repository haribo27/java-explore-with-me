package ru.practicum.dto.mainservice.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class EventMapperContext {

    private Map<Long, Long> viewsMap;
    private Map<Long, Long> commentsCount;
}