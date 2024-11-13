package ru.practicum.dto.mainservice.service;

import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitStatsDto;
import ru.practicum.dto.mainservice.dto.event.*;
import ru.practicum.dto.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.dto.mainservice.exception.ConditionsAreNotMet;
import ru.practicum.dto.mainservice.exception.EntityNotFoundException;
import ru.practicum.dto.mainservice.exception.EventStatusInvalid;
import ru.practicum.dto.mainservice.exception.IncorrectInputArguments;
import ru.practicum.dto.mainservice.mapper.EventMapper;
import ru.practicum.dto.mainservice.model.*;
import ru.practicum.dto.mainservice.repository.CategoryRepository;
import ru.practicum.dto.mainservice.repository.EventRepository;
import ru.practicum.dto.mainservice.repository.UserRepository;
import ru.practicum.dto.mainservice.viewsApiClient.ApiClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final ApiClient apiClient;

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
        event.setCreatedOn(LocalDateTime.now().withNano(0));
        log.info("Set event status: {} to event", PENDING);
        event.setState(PENDING);
        event = eventRepository.save(event);
        log.info("Saved event: {}, id: {}", event, event.getId());
        return eventMapper.mapToEventFullDto(event, new HashMap<>());
    }

    @Override
    public List<EventShortDto> findEventsByParams(long userId, Integer from, Integer size) {
        log.info("Finding events by params: userid: {}, from: {}, size: {}", userId, from, size);
        Pageable pageable = PageRequest.of(from, size);
        List<Event> findEvents = eventRepository.findAllByInitiator_Id(userId, pageable);
        Map<Long, Long> viewsMap = getEventViewsMap(findEvents.stream().map(Event::getId).toList(),
                findEarliestEvent(findEvents.stream().filter(event -> event.getState().equals(PUBLISHED)).toList()));
        return findEvents.stream()
                .map(event -> eventMapper.mapToShortEventDto(event, viewsMap))
                .toList();
    }

    @Override
    public List<EventFullDto> findEventsByParams(Set<Long> users, Set<EventState> states,
                                                 Set<Long> categories, LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd, Integer from, Integer size) {
        log.info("Finding events with params...");
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
        List<Event> findEvents = query.fetch();
        Map<Long, Long> viewsMap = getEventViewsMap(findEvents.stream().map(Event::getId).toList(),
                findEarliestEvent(findEvents.stream().filter(event1 -> event1.getState().equals(PUBLISHED)).toList()));
        return findEvents
                .stream().map(event1 -> eventMapper.mapToEventFullDto(event1, viewsMap))
                .toList();
    }

    @Override
    public EventFullDto getFullInfoAboutUserEvent(long userId, long eventId) {
        log.info("Getting full info about user's {} event {}", userId, eventId);
        Event event = eventRepository.findEventByInitiator_IdAndId(userId, eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " not found"));
        Map<Long, Long> viewsMap = getEventViewsMap(List.of(eventId), findEarliestEvent(List.of(event)));
        return eventMapper.mapToEventFullDto(event, viewsMap);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(UpdateEventUserRequest requestEventDto, long eventId, long userId) {
        log.info("Updating event id: {}, updated fields: {}", eventId, requestEventDto);
        Event event = eventRepository.findEventByInitiator_IdAndId(userId, eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " not found"));
        checkThatEventDateAfterTwoHours(requestEventDto);
        checkEventStatusAndChangeIt(requestEventDto, event);
        event = eventRepository.save(event);
        log.info("Event updated and saved success {}", event);
        Map<Long, Long> viewsMap = getEventViewsMap(List.of(eventId), findEarliestEvent(List.of(event)));
        return eventMapper.mapToEventFullDto(event, viewsMap);
    }

    @Override
    public List<ParticipationRequestDto> getOwnersEventRequests(long userId, long eventId) {
        log.info("Getting requests from User: {} own event: {}", userId, eventId);
        return requestService.getOwnersEventRequests(userId, eventId);
    }

    @Override
    @Transactional
    public void updateRequestsStatusUsersOwnEvents(EventRequestStatusUpdateRequest dto, long userId, long eventId) {
        log.info("Changing status of event's requests. Event owner: {},  Event id: {}", userId, eventId);
        requestService.updateRequestsStatus(dto, userId, eventId);
    }

    @Override
    @Transactional
    public EventFullDto adminUpdateEvent(UpdateEventAdminRequest updateRequest, long eventId) {
        log.info("Admin updating event: {} , updated body: {}", eventId, updateRequest);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " not found"));
        if (updateRequest.getEventDate() != null && event.getPublishedOn() != null) {
            checkEventDateIsValid(updateRequest, event);
        }
        eventMapper.updateEventAdminRequest(updateRequest, event);
        if (updateRequest.getStateAction() != null) {
            changeStatusFromRequest(updateRequest, event);
        }
        event = eventRepository.save(event);
        Map<Long, Long> viewsMap = getEventViewsMap(List.of(eventId), findEarliestEvent(List.of(event)));
        return eventMapper.mapToEventFullDto(event, viewsMap);
    }

    private void checkEventDateIsValid(UpdateEventAdminRequest updateRequest, Event event) {
        log.info("Check that is event date is correct: {}", updateRequest.getEventDate());
        if (updateRequest.getEventDate() != null &&
                updateRequest.getEventDate().isBefore(event.getPublishedOn().plusHours(1))) {
            log.warn("Event date is incorrect");
            throw new IncorrectInputArguments("Дата начала события должна быть не ранее чем за час до даты публикации.");
        }
    }

    private void changeStatusFromRequest(UpdateEventAdminRequest updateRequest, Event event) {
        switch (updateRequest.getStateAction()) {
            case PUBLISH_EVENT:
                if (event.getState().equals(PENDING)) {
                    event.setState(PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    log.info("Admin change event status from: {}, to: {}", PENDING, PUBLISHED);
                } else {
                    log.warn("Event can't published cause status is: {} or {}", PUBLISHED, CANCELED);
                    throw new ConditionsAreNotMet("Cannot publish the event because" +
                            " it's not in the right state: PUBLISHED or CANCELED");
                }
                break;
            case REJECT_EVENT:
                if (event.getState().equals(PUBLISHED)) {
                    log.warn("Event with status: {} can't be REJECTED", PUBLISHED);
                    throw new ConditionsAreNotMet("You cant reject PUBLISHED event");
                }
                if (event.getState().equals(PENDING)) {
                    event.setState(CANCELED);
                    log.info("Admin change event status to: {}", CANCELED);
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
                .collect(Collectors.toSet()));
        dto.setRejectedRequests(test.stream()
                .filter(request -> request.getStatus().equals(RequestState.REJECTED))
                .collect(Collectors.toSet()));
        return dto;
    }

    @Override
    public List<EventShortDto> findEventsByParamsAndFilter(String text, Set<Long> categories, Boolean paid,
                                                           LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                           Boolean onlyAvailable, String sort, Integer from,
                                                           Integer size, HttpServletRequest request) {
        QEvent event = QEvent.event;
        JPAQuery<Event> query = new JPAQuery<>(entityManager);

        query.from(event);

        if (rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new IncorrectInputArguments("End date of event cant be before start date");
        }

        if (text != null && !text.isEmpty()) {
            query.where(event.annotation.containsIgnoreCase(text)
                    .or(event.description.containsIgnoreCase(text)));
        }

        if (categories != null && !categories.isEmpty()) {
            query.where(event.category.id.in(categories));
        }

        if (paid != null) {
            query.where(event.paid.eq(paid));
        }

        if (rangeStart != null) {
            query.where(event.eventDate.goe(rangeStart));
        } else {
            query.where(event.eventDate.goe(LocalDateTime.now()));
        }
        if (rangeEnd != null) {
            query.where(event.eventDate.loe(rangeEnd));
        }
        if (onlyAvailable != null && onlyAvailable) {
            query.where(event.confirmedRequests.lt(event.participantLimit));
        }
        if (from != null) {
            query.offset(from);
        }
        if (size != null) {
            query.limit(size);
        }

        List<Event> findEvents = query.fetch();
        apiClient.sendHitRequestToApi(request);
        Map<Long, Long> viewsMap = getEventViewsMap(findEvents.stream().map(Event::getId).toList(),
                findEarliestEvent(findEvents));
        return findEvents.stream().map(event1 -> eventMapper.mapToShortEventDto(event1, viewsMap)).toList();
    }

    @Override
    public EventFullDto findById(long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + id + " not found"));
        if (event.getState().equals(PUBLISHED)) {
            apiClient.sendHitRequestToApi(request);
        } else {
            throw new EventStatusInvalid("Event must be PUBLISHED");
        }
        Map<Long, Long> viewsMap = getEventViewsMap(List.of(id), findEarliestEvent(List.of(event)));
        return eventMapper.mapToEventFullDto(event, viewsMap);
    }

    private Map<Long, Long> getEventViewsMap(List<Long> eventIds, LocalDateTime earliestEventDate) {
        List<HitStatsDto> statsList = apiClient.getEventViews(eventIds, earliestEventDate);
        if (statsList == null) {
            return Collections.emptyMap();
        }
        return statsList.stream()
                .collect(Collectors.toMap(
                        hit -> Long.parseLong(hit.getUri().split("/events/")[1]),
                        HitStatsDto::getHits
                ));
    }

    private void checkEventStatusAndChangeIt(UpdateEventUserRequest requestEventDto, Event event) {
        if (event.getState().equals(PUBLISHED)) {
            log.info("Event is published, cant update this event");
            throw new ConditionsAreNotMet("Only pending or canceled events can be changed");
        } else if (requestEventDto.getStateAction() != null) {
            switch (requestEventDto.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(CANCELED);
                    break;
            }
            eventMapper.updateEvent(requestEventDto, event);
        }
    }

    private void checkThatEventDateAfterTwoHours(UpdateEventUserRequest requestEventDto) {
        if (requestEventDto.getEventDate() != null &&
                requestEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IncorrectInputArguments("Дата начала события должна быть не " +
                    "ранее чем за два часа до даты публикации.");
        }
    }

    private LocalDateTime findEarliestEvent(List<Event> events) {
        if (events == null) {
            return null;
        }
        if (events.size() == 1) {
            return events.getFirst().getPublishedOn();
        }
        Optional<Event> earliestEvent = events.stream().min(Comparator.comparing(Event::getPublishedOn));
        return earliestEvent.map(Event::getPublishedOn).orElse(null);
    }
}
