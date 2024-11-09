package ru.practicum.dto.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.dto.mainservice.exception.ConditionsAreNotMet;
import ru.practicum.dto.mainservice.exception.EntityNotFoundException;
import ru.practicum.dto.mainservice.mapper.RequestMapper;
import ru.practicum.dto.mainservice.model.Event;
import ru.practicum.dto.mainservice.model.Request;
import ru.practicum.dto.mainservice.model.User;
import ru.practicum.dto.mainservice.repository.EventRepository;
import ru.practicum.dto.mainservice.repository.RequestRepository;
import ru.practicum.dto.mainservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.dto.mainservice.model.EventState.PUBLISHED;
import static ru.practicum.dto.mainservice.model.RequestState.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public ParticipationRequestDto addRequestToEvent(long userId, long eventId) {
        log.info("Add new request to eventId: {}, from user: {}", eventId, userId);
        Request request = new Request();
        Event event = eventRepository.findEventByIdWithInitiator(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " not found"));
        log.info("Get event from request: {}", event);
        log.info("Validating request...");
        validateRequest(event, request, userId, eventId);
        log.info("Validation success!");
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + userId + " was not found"));
        log.info("Get requester: {}", requester);
        setRequestFields(request, event, requester);
        request = requestRepository.save(request);
        log.info("Saved request: {}", request);
        eventRepository.updateConfirmedRequests(eventId);
        log.info("Updating confirmed_requests to event");
        return requestMapper.mapToDto(request);
    }

    @Override
    public List<ParticipationRequestDto> findUsersRequests(long userId) {
        log.info("Find user's requests: {}", userId);
        return requestRepository.findUsersRequests(userId)
                .stream()
                .map(requestMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        log.info("Cancel userId: {} requestId: {}", userId, requestId);
        Request request = requestRepository.findByIdAndRequester_Id(requestId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Request with id=" + requestId + " not found"));
        request.setStatus(CANCELED);
        log.info("Set request state Canceled to request");
        request = requestRepository.save(request);
        log.info("Request updated: {}", request);
        eventRepository.updateConfirmedRequests(request.getEvent().getId());
        log.info("Updating confirmed_requests to event");
        return requestMapper.mapToDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getOwnersEventRequests(long userId, long eventId) {
        log.info("Getting events owner requests. Owner: {}, Event: {}", userId, eventId);
        return requestRepository.getOwnersEventRequests(userId, eventId)
                .stream()
                .map(requestMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateRequestsStatus(EventRequestStatusUpdateRequest dto, long userId, long eventId) {
        // TODO
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " not found"));
        List<Request> requests = requestRepository.getOwnersEventRequests(userId, eventId);

        checkIdsIsCorrects(dto, requests);

        if (!event.getRequestModeration()) return;

        if (event.getParticipantLimit() == 0) return;

        checkEventParticipantLimit(event);

        int limit = countRequestsLimit(event);

        List<Request> acceptedRequests = requests.stream()
                .filter(request -> dto.getRequestIds().contains(request.getId()))
                .limit(limit)
                .toList();

        List<Request> requestsTorReject = requests.stream()
                .filter(request -> !acceptedRequests.contains(request))
                .toList();

        if (acceptedRequests.stream().anyMatch(request -> request.getStatus().equals(CONFIRMED) || request.getStatus().equals(CANCELED))) {
            throw new ConditionsAreNotMet("CONFIRMED or CANCELED request can't updated");
        }

        acceptedRequests.forEach(request -> {
            request.setStatus(dto.getStatus());
            requestRepository.save(request);
            eventRepository.updateConfirmedRequests(eventId);
        });

        requestsTorReject.forEach(request -> {
            request.setStatus(REJECTED);
            requestRepository.save(request);
            eventRepository.updateConfirmedRequests(eventId);
        });
        log.info("Updating success");
    }

    @Override
    public List<ParticipationRequestDto> getUpdatedRequests(List<Long> requestIds, long userId, long evenId) {
        return requestRepository.findRequestByIdIn(requestIds)
                .stream()
                .map(requestMapper::mapToDto)
                .toList();
    }

    private static void checkIdsIsCorrects(EventRequestStatusUpdateRequest dto, List<Request> requests) {
        Set<Long> requestIds = requests.stream()
                .map(Request::getId)
                .collect(Collectors.toSet());

        boolean allMatch = requestIds.containsAll(dto.getRequestIds());

        if (!allMatch) {
            throw new ConditionsAreNotMet("Input ids is invalid");
        }
    }

    private int countRequestsLimit(Event event) {
        return event.getParticipantLimit() - event.getConfirmedRequests();
    }

    private static void checkEventParticipantLimit(Event event) {
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ConditionsAreNotMet("The participant limit has been reached");
        }
    }

    private static void setRequestFields(Request request, Event event, User requester) {
        request.setEvent(event);
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now().withNano(0));
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus(CONFIRMED);
        } else {
            request.setStatus(PENDING);
        }
    }

    private void validateRequest(Event event, Request request,
                                 long userId, long eventId) {
        Optional<Request> maybeRequest = requestRepository.findByRequesterIdAndEvent_Id(userId, eventId);
        if (maybeRequest.isPresent()) {
            log.debug("User already has request to this event");
            throw new ConditionsAreNotMet("You can't make second request to event. User id:" + userId);
        }

        if (event.getInitiator().getId() == userId) {
            log.debug("User is initiator of this event. Can't create request");
            throw new ConditionsAreNotMet("Initiator of event can't make request to this event");
        }
        if (!event.getState().equals(PUBLISHED)) {
            log.debug("If event state not equals PUBLISHED you cat create new request");
            throw new ConditionsAreNotMet("You can't participate in an unpublished event");
        }
        if (event.getParticipantLimit() > 0) {
            log.debug("Check partitions limit of event");
            long requestsCount = requestRepository.getPartitionsRequestToEvent(eventId);
            log.debug("Requests count: {}", requestsCount);
            if (requestsCount == event.getParticipantLimit()) {
                log.debug("Requests count = partitions limit, cant create request");
                throw new ConditionsAreNotMet("The limit of participation requests has been reached.");
            }
        }
        if (!event.getRequestModeration()) {
            log.info("Moderation required = false ? request state: approved");
            request.setStatus(CONFIRMED);
        } else {
            log.info("Moderation required = true ? request state: pending");
            request.setStatus(PENDING);
        }
    }
}
