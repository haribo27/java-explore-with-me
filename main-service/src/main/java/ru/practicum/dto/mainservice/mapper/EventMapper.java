package ru.practicum.dto.mainservice.mapper;

import org.mapstruct.*;
import ru.practicum.dto.mainservice.dto.comment.CommentDto;
import ru.practicum.dto.mainservice.dto.event.*;
import ru.practicum.dto.mainservice.model.Event;
import ru.practicum.dto.mainservice.service.CommentService;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    @Mapping(target = "category", ignore = true)
    Event mapToEvent(RequestEventDto requestEventDto);

    EventFullDto mapToEventFullDto(Event event, @Context Map<Long, Long> viewsMap, @Context CommentService commentService);

    EventShortDto mapToShortEventDto(Event event, @Context Map<Long, Long> viewsMap, @Context CommentService commentService);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "state", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEvent(UpdateEventUserRequest requestDto, @MappingTarget Event event);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "state", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventAdminRequest(UpdateEventAdminRequest request, @MappingTarget Event event);

    @AfterMapping
    default void addCommentsAndViewsToEventDto(@MappingTarget EventFullDto dto, @Context Map<Long, Long> viewsMap, @Context CommentService commentService) {
        Set<CommentDto> comments = commentService.getEventComments(Collections.singletonList(dto.getId()));
        dto.setViews(viewsMap.getOrDefault(dto.getId(), 0L));
        dto.setComments(comments);
    }

    @AfterMapping
    default void setViews(@MappingTarget EventShortDto dto, @Context Map<Long, Long> viewsMap, @Context CommentService commentService) {
        Set<CommentDto> comments = commentService.getEventComments(Collections.singletonList(dto.getId()));
        dto.setViews(viewsMap.getOrDefault(dto.getId(), 0L));
        dto.setComments(comments);
    }


}
