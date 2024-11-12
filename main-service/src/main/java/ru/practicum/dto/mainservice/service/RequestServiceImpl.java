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
import java.util.*;
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
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " not found"));
        List<Request> requests = requestRepository.getOwnersEventRequests(userId, eventId);
        checkIdsIsCorrects(dto, requests);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            log.debug("Event with requestModeration = false or ParticipantLimit = 0 don't need update status");
            return;
        }
        checkEventParticipantLimit(event);
        List<Request> requestsToConfirm;
        List<Request> requestsTorReject = new ArrayList<>();
        int availableRequestsLimit = countAvailableRequestsLimit(event);
        log.info("Available request limit = {}", availableRequestsLimit);
        checkRequestsToUpdateStatus(requests);
        requestsToConfirm = requests.stream()
                .filter(request -> dto.getRequestIds().contains(request.getId()))
                .limit(availableRequestsLimit)
                .peek(request -> request.setStatus(dto.getStatus()))
                .toList();
        log.info("Requests to confirm {}", requestsToConfirm);
        if (dto.getStatus().equals(CONFIRMED)) {
            requestsTorReject = requests.stream()
                    .filter(request -> !requestsToConfirm.contains(request))
                    .peek(request -> request.setStatus(REJECTED))
                    .toList();
            log.info("Requests to reject: {}", requestsTorReject);
        }
        requestRepository.saveAll(requestsToConfirm);
        requestRepository.saveAll(requestsTorReject);
        log.info("Updating success");
    }

    @Override
    public List<ParticipationRequestDto> getUpdatedRequests(List<Long> requestIds, long userId, long evenId) {
        log.info("Getting updated requests ids: {}", requestIds);
        return requestRepository.findRequestByIdIn(requestIds)
                .stream()
                .map(requestMapper::mapToDto)
                .toList();
    }

    private static void checkIdsIsCorrects(EventRequestStatusUpdateRequest dto, List<Request> requests) {
        log.debug("Check that input ids are correct");

        Set<Long> requestIds = requests.stream()
                .map(Request::getId)
                .collect(Collectors.toSet());

        if (!requestIds.containsAll(new HashSet<>(dto.getRequestIds()))) {
            log.warn("Input IDS is incorrect");
            throw new ConditionsAreNotMet("Input ids are invalid");
        }
    }

    private int countAvailableRequestsLimit(Event event) {
        return event.getParticipantLimit() - event.getConfirmedRequests();
    }

    private void checkEventParticipantLimit(Event event) {
        log.debug("Check that the number of applications for participation has not been exceeded");
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            log.warn("The number of confirmed requests is equal to the number of participants");
            throw new ConditionsAreNotMet("The participant limit has been reached");
        }
    }

    private void setRequestFields(Request request, Event event, User requester) {
        log.debug("Setting request fields");
        request.setEvent(event);
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now().withNano(0));
    }

    private void validateRequest(Event event, Request request,
                                 long userId, long eventId) {
        Optional<Request> maybeRequest = requestRepository.findByRequesterIdAndEvent_Id(userId, eventId);
        if (maybeRequest.isPresent()) {
            log.warn("User already has request to this event");
            throw new ConditionsAreNotMet("You can't make second request to event. User id:" + userId);
        }

        if (event.getInitiator().getId() == userId) {
            log.warn("User is initiator of this event. Can't create request");
            throw new ConditionsAreNotMet("Initiator of event can't make request to this event");
        }
        if (!event.getState().equals(PUBLISHED)) {
            log.warn("If event state not equals PUBLISHED you cat create new request");
            throw new ConditionsAreNotMet("You can't participate in an unpublished event");
        }
        if (event.getParticipantLimit() > 0) {
            log.debug("Check partitions limit of event");
            if (event.getConfirmedRequests() == event.getParticipantLimit()) {
                log.warn("Requests count = partitions limit, cant create request");
                throw new ConditionsAreNotMet("The limit of participation requests has been reached.");
            }
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            log.info("Moderation required = false ? request state: approved");
            request.setStatus(CONFIRMED);
        } else {
            log.info("Moderation required = true ? request state: pending");
            request.setStatus(PENDING);
        }
    }

    private void checkRequestsToUpdateStatus(List<Request> requestsToConfirm) {
        if (requestsToConfirm.stream().anyMatch(request -> request.getStatus().equals(CONFIRMED) ||
                request.getStatus().equals(CANCELED))) {
            log.warn("Requests with status {} and {} can't updated", CANCELED, CONFIRMED);
            throw new ConditionsAreNotMet("CONFIRMED or CANCELED request can't updated");
        }
    }
}
