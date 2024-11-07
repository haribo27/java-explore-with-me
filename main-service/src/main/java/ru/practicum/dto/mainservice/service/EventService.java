package ru.practicum.dto.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.dto.mainservice.dto.event.*;
import ru.practicum.dto.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.dto.mainservice.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto addEvent(RequestEventDto requestEventDto, long userId);

    List<EventShortDto> findEventsByParams(long userId, Integer from, Integer size);

    List<EventFullDto> findEventsByParams(List<Long> users, List<EventState> states,
                                          List<Long> categories, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Integer from,
                                          Integer size);

    EventFullDto getFullInfoAboutUserEvent(long userId, long eventId);

    EventFullDto updateEvent(UpdateRequestEventDto requestEventDto, long eventId, long userId);

    List<ParticipationRequestDto> getOwnersEventRequests(long userId, long eventId);

    void updateRequestsStatusUsersOwnEvents(EventRequestStatusUpdateRequest dto, long userId, long eventId);

    EventFullDto adminUpdateEvent(UpdateEventAdminRequest updateRequest, long eventId);

    EventRequestStatusUpdateResult getUpdatedRequestStatus(List<Long> requestIds,
                                                           long userId, long evenId);

    List<EventShortDto> findEventsByParamsAndFilter(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);

    EventFullDto findById(long id, HttpServletRequest request);
}
