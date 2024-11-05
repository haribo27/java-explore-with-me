package ru.practicum.dto.mainservice.service;

import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.mainservice.dto.event.*;
import ru.practicum.dto.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.dto.mainservice.exception.ConditionsAreNotMet;
import ru.practicum.dto.mainservice.exception.EntityNotFoundException;
import ru.practicum.dto.mainservice.exception.IncorrectInputArguments;
import ru.practicum.dto.mainservice.mapper.EventMapper;
import ru.practicum.dto.mainservice.model.*;
import ru.practicum.dto.mainservice.repository.CategoryRepository;
import ru.practicum.dto.mainservice.repository.EventRepository;
import ru.practicum.dto.mainservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.dto.mainservice.model.EventState.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestService requestService;
    private final EventMapper eventMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public EventFullDto addEvent(RequestEventDto requestEventDto, long userId) {
        log.info("Adding new Event {}, author id: {}", requestEventDto, userId);
        Event event = eventMapper.mapToEvent(requestEventDto);
        log.info("Getting category from request id: {}", requestEventDto.getCategory());
        Category eventCategory = categoryRepository.findById(requestEventDto.getCategory())
                .orElseThrow(() -> new EntityNotFoundException("Category with id=" +
                        requestEventDto.getCategory() + " was not found"));
        log.info("Getting event initiator from request with id: {}", userId);
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" +
                        userId + " was not found"));
        log.info("Set category to event {}", eventCategory);
        event.setCategory(eventCategory);
        log.info("Set initiator to event {}", initiator);
        event.setInitiator(initiator);
        log.info("Set created date {}", LocalDateTime.now());
        event.setCreatedOn(LocalDateTime.now());

        log.info("Set event state to event");
        if (event.getRequestModeration()) {
            log.info("Event need request moderation, state: {}", EventState.PENDING);
            event.setState(EventState.PENDING);
        } else {
            log.info("Dont need moderation, state: {}", PUBLISHED);
            event.setState(PUBLISHED);
            log.info("Setting published date time {}", LocalDateTime.now());
            event.setPublishedOn(LocalDateTime.now());
        }
        log.info("Saving event: {}", event);
        event = eventRepository.save(event);
        return eventMapper.mapToEventFullDto(event);
    }

    @Override
    public List<EventShortDto> findEventsByParams(long userId, Integer from, Integer size) {
        log.info("Find events by params: userid: {}, from: {}, size: {}", userId, from, size);
        List<Event> resultList = eventRepository.findEvents(userId, from, size);
        log.info("Result list: {}", resultList);
        return resultList.stream()
                .map(eventMapper::mapToShortEventDto)
                .toList();
    }

    @Override
    public List<EventFullDto> findEventsByParams(List<Long> users, List<EventState> states,
                                                 List<Long> categories, LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd, Integer from, Integer size) {
        QEvent event = QEvent.event;
        JPAQuery<Event> query = new JPAQuery<>(entityManager);
        query.from(event);
        if (users != null && !users.isEmpty()) {
            query.where(event.initiator.id.in(users));
        }
        if (states != null && !states.isEmpty()) {
            query.where(event.state.in(states));
        }
        if (categories != null && !categories.isEmpty()) {
            query.where(event.category.id.in(categories));
        }
        if (rangeStart != null) {
            query.where(event.eventDate.goe(rangeStart));
        }
        if (rangeEnd != null) {
            query.where(event.eventDate.loe(rangeEnd));
        }
        if (from != null) {
            query.offset(from);
        }
        if (size != null) {
            query.limit(size);
        }
        return query.fetch()
                .stream().map(eventMapper::mapToEventFullDto)
                .toList();
    }

    @Override
    public EventFullDto getFullInfoAboutUserEvent(long userId, long eventId) {
        log.info("Get full info about user's event");
        Event event = eventRepository.findEventByInitiator_IdAndId(userId, eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " not found"));
        return eventMapper.mapToEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(UpdateRequestEventDto requestEventDto, long eventId, long userId) {
        log.info("Updating event id: {}, updated fields: {}", eventId, requestEventDto);
        Event event = eventRepository.findEventByInitiator_IdAndId(userId, eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " not found"));
        if (requestEventDto.getEventDate() != null &&
                requestEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IncorrectInputArguments("Дата начала события должна быть не " +
                    "ранее чем за два часа до даты публикации.");
        }

        if (event.getState().equals(PUBLISHED)) {
            log.info("Event is published, cant update this event");
            throw new ConditionsAreNotMet("Only pending or canceled events can be changed");
        } else if (requestEventDto.getStateAction() != null) {
            switch (requestEventDto.getStateAction()) {
                case "SEND_TO_REVIEW":
                    event.setState(PENDING);
                    break;
                case "CANCEL_REVIEW":
                    event.setState(CANCELED);
                    break;
            }
            eventMapper.updateEvent(requestEventDto, event);
        }
        event = eventRepository.save(event);
        log.info("Event updated and saved success {}", event);
        return eventMapper.mapToEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getOwnersEventRequests(long userId, long eventId) {
        log.info("Getting requests from User: {} own event: {}", userId, eventId);
        return requestService.getOwnersEventRequests(userId, eventId);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestsStatusUsersOwnEvents(EventRequestStatusUpdateRequest dto, long userId, long eventId) {
        log.info("Changing status of event's requests. Event owner: {},  Event id: {}", userId, eventId);
        requestService.updateRequestsStatus(dto, userId, eventId);
        EventRequestStatusUpdateResult test = getUpdatedRequestStatus(dto.getRequestIds(),userId, eventId);
        return getUpdatedRequestStatus(dto.getRequestIds(),userId, eventId);
    }

    @Override
    @Transactional
    public EventFullDto adminUpdateEvent(UpdateEventAdminRequest updateRequest, long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " not found"));
        if (updateRequest.getEventDate() != null) {
            checkEventDateIsValid(updateRequest, event);
        }
        eventMapper.updateEventAdminRequest(updateRequest, event);
        if (updateRequest.getStateAction() != null) {
            changeStatusFromRequest(updateRequest, event);
        }
        event = eventRepository.save(event);
        return eventMapper.mapToEventFullDto(event);
    }

    private void checkEventDateIsValid(UpdateEventAdminRequest updateRequest, Event event) {
        if (updateRequest.getEventDate() != null &&
                updateRequest.getEventDate().isBefore(event.getPublishedOn().plusHours(1))) {
            throw new IncorrectInputArguments("Дата начала события должна быть не ранее чем за час до даты публикации.");
        }
    }

    private void changeStatusFromRequest(UpdateEventAdminRequest updateRequest, Event event) {
        switch (updateRequest.getStateAction()) {
            case "PUBLISH_EVENT":
                if (event.getState().equals(PENDING)) {
                    event.setState(PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else {
                    throw new ConditionsAreNotMet("Cannot publish the event because" +
                            " it's not in the right state: PUBLISHED or CANCELED");
                }
                break;
            case "REJECT_EVENT":
                if (event.getState().equals(PENDING)) {
                    event.setState(CANCELED);
                } else {
                    throw new ConditionsAreNotMet("Cannot reject the event because " +
                            "it's not in the right state: PUBLISHED or CANCELED");
                }
                break;
        }
    }

    @Override
    public EventRequestStatusUpdateResult getUpdatedRequestStatus(List<Long> requestIds,
                                                                  long userId, long evenId) {
        List<ParticipationRequestDto> test = requestService.getUpdatedRequests(requestIds, userId, evenId);
        EventRequestStatusUpdateResult dto = new EventRequestStatusUpdateResult();
        dto.setConfirmedRequests(test.stream()
                .filter(request -> request.getStatus().equals(RequestState.CONFIRMED))
                .toList());
        dto.setRejectedRequests(test.stream()
                .filter(request -> request.getStatus().equals(RequestState.REJECTED))
                .toList());
        return dto;
    }
}