package ru.practicum.dto.mainservice.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.dto.mainservice.dto.event.*;
import ru.practicum.dto.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.dto.mainservice.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {

    EventFullDto addEvent(RequestEventDto requestEventDto, long userId);

    List<EventShortDto> findEventsByParams(long userId, Integer from, Integer size);

    List<EventFullDto> findEventsByParams(Set<Long> users, Set<EventState> states,
                                          Set<Long> categories, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Integer from,
                                          Integer size);

    EventFullDto getFullInfoAboutUserEvent(long userId, long eventId);

    EventFullDto updateEvent(UpdateEventUserRequest requestEventDto, long eventId, long userId);

    List<ParticipationRequestDto> getOwnersEventRequests(long userId, long eventId);

    void updateRequestsStatusUsersOwnEvents(EventRequestStatusUpdateRequest dto, long userId, long eventId);

    EventFullDto adminUpdateEvent(UpdateEventAdminRequest updateRequest, long eventId);

    EventRequestStatusUpdateResult getUpdatedRequestStatus(List<Long> requestIds,
                                                           long userId, long evenId);

    List<EventShortDto> findEventsByParamsAndFilter(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                                    Integer size,HttpServletRequest request);

    EventFullDto findById(long id, HttpServletRequest request);
}
