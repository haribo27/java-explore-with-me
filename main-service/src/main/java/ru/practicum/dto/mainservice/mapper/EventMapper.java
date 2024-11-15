package ru.practicum.dto.mainservice.mapper;

import org.mapstruct.*;
import ru.practicum.dto.mainservice.dto.event.*;
import ru.practicum.dto.mainservice.model.Event;
import ru.practicum.dto.mainservice.util.EventMapperContext;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    @Mapping(target = "category", ignore = true)
    Event mapToEvent(RequestEventDto requestEventDto);

    EventFullDto mapToEventFullDto(Event event, @Context EventMapperContext eventContext);

    EventShortDto mapToShortEventDto(Event event, @Context EventMapperContext eventContext);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "state", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEvent(UpdateEventUserRequest requestDto, @MappingTarget Event event);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "state", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventAdminRequest(UpdateEventAdminRequest request, @MappingTarget Event event);

    @AfterMapping
    default void addCommentsAndViewsToEventDto(@MappingTarget EventFullDto dto, @Context EventMapperContext eventContext) {
        dto.setCommentsCount(eventContext.getCommentsCount().getOrDefault(dto.getId(), 0L));
        dto.setViews(eventContext.getViewsMap().getOrDefault(dto.getId(), 0L));
    }

    @AfterMapping
    default void setViews(@MappingTarget EventShortDto dto, @Context EventMapperContext eventContext) {
        dto.setCommentsCount(eventContext.getCommentsCount().getOrDefault(dto.getId(), 0L));
        dto.setViews(eventContext.getViewsMap().getOrDefault(dto.getId(), 0L));
    }


}
