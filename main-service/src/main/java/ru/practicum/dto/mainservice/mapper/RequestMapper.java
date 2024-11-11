package ru.practicum.dto.mainservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.dto.mainservice.model.Event;
import ru.practicum.dto.mainservice.model.Request;
import ru.practicum.dto.mainservice.model.User;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "event", source = "event")
    @Mapping(target = "requester", source = "requester")
    ParticipationRequestDto mapToDto(Request request);

    default Long map(Event event) {
        return event != null ? event.getId() : null;
    }

    default Long map(User user) {
        return user != null ? user.getId() : null;
    }
}
