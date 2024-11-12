package ru.practicum.dto.mainservice.controllers.privates;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.dto.event.*;
import ru.practicum.dto.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.dto.mainservice.service.EventService;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class PrivateEventController {

    private final EventService eventService;

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> addEvent(@RequestBody @Valid RequestEventDto requestEventDto,
                                                 @PathVariable long userId) {
        return new ResponseEntity<>(eventService.addEvent(requestEventDto, userId),
                HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventShortDto>> getUsersEvents(@PathVariable long userId,
                                                              @RequestParam(name = "from", defaultValue = "0")
                                                              @PositiveOrZero Integer from,
                                                              @RequestParam(name = "size", defaultValue = "10")
                                                              @Positive Integer size) {
        return ResponseEntity.ok(eventService.findEventsByParams(userId, from, size));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getFullInfoAboutUserEvent(@PathVariable long userId,
                                                                  @PathVariable long eventId) {
        return ResponseEntity.ok(eventService.getFullInfoAboutUserEvent(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@RequestBody @Valid UpdateEventUserRequest requestEventDto,
                                                    @PathVariable long userId,
                                                    @PathVariable long eventId) {
        return ResponseEntity.ok(eventService.updateEvent(requestEventDto, eventId, userId));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getOwnersEventRequests(@PathVariable(name = "userId") long userId,
                                                                                @PathVariable(name = "eventId") long eventId) {
        return ResponseEntity.ok(eventService.getOwnersEventRequests(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequestsStatusUsersOwnEvents(@RequestBody EventRequestStatusUpdateRequest dto,
                                                                                             @PathVariable(name = "userId") long userId,
                                                                                             @PathVariable(name = "eventId") long eventId) {
        eventService.updateRequestsStatusUsersOwnEvents(dto, userId, eventId);
        return ResponseEntity.ok(eventService.getUpdatedRequestStatus(dto.getRequestIds(), userId, eventId));
    }
}
