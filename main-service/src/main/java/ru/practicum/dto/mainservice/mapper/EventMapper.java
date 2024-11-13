package ru.practicum.dto.mainservice.mapper;

import org.mapstruct.*;
import ru.practicum.dto.mainservice.dto.event.*;
import ru.practicum.dto.mainservice.model.Event;

import java.util.Map;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    @Mapping(target = "category", ignore = true)
    Event mapToEvent(RequestEventDto requestEventDto);

    EventFullDto mapToEventFullDto(Event event, @Context Map<Long, Long> viewsMap);

    EventShortDto mapToShortEventDto(Event event, @Context Map<Long, Long> viewsMap);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "state", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEvent(UpdateEventUserRequest requestDto, @MappingTarget Event event);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "state", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventAdminRequest(UpdateEventAdminRequest request, @MappingTarget Event event);

    @AfterMapping
    default void setViews(@MappingTarget EventFullDto dto, @Context Map<Long, Long> viewsMap) {
        dto.setViews(viewsMap.getOrDefault(dto.getId(), 0L));
    }

    @AfterMapping
    default void setViews(@MappingTarget EventShortDto dto, @Context Map<Long, Long> viewsMap) {
        dto.setViews(viewsMap.getOrDefault(dto.getId(), 0L));
    }
}
