package ru.practicum.dto.mainservice.service;

import ru.practicum.dto.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.mainservice.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto addRequestToEvent(long userId, long eventId);

    List<ParticipationRequestDto> findUsersRequests(long userId);

    ParticipationRequestDto cancelRequest(long userId, long requesterId);

    List<ParticipationRequestDto> getOwnersEventRequests(long userId, long eventId);

    void updateRequestsStatus(EventRequestStatusUpdateRequest dto, long userId, long eventId);

    List<ParticipationRequestDto> getUpdatedRequests(List<Long> requestIds, long userId, long evenId);
}
