package ru.practicum.dto.mainservice.controllers.privates;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.mainservice.dto.event.RequestEventDto;
import ru.practicum.dto.mainservice.dto.event.UpdateRequestEventDto;
import ru.practicum.dto.mainservice.service.EventService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService eventService;

    @PostMapping("/{userId}/events")
    public ResponseEntity<?> addEvent(@RequestBody @Valid RequestEventDto requestEventDto,
                                      @PathVariable long userId) {
        return new ResponseEntity<>(eventService.addEvent(requestEventDto, userId),
                HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/events")
    public ResponseEntity<?> getUsersEvents(@PathVariable long userId,
                                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return new ResponseEntity<>(eventService.findEventsByParams(userId, from, size),
                HttpStatus.OK);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<?> getFullInfoAboutUserEvent(@PathVariable long userId,
                                                       @PathVariable long eventId) {
        return new ResponseEntity<>(eventService.getFullInfoAboutUserEvent(userId, eventId),
                HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<?> updateEvent(@RequestBody @Valid UpdateRequestEventDto requestEventDto,
                                         @PathVariable long userId,
                                         @PathVariable long eventId) {
        // todo доделать тут неправильно работает
        return new ResponseEntity<>(eventService.updateEvent(requestEventDto, eventId, userId),
                HttpStatus.OK);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<?> getOwnersEventRequests(@PathVariable(name = "userId") long userId,
                                                    @PathVariable(name = "eventId") long eventId) {
        return new ResponseEntity<>(eventService.getOwnersEventRequests(userId, eventId),
                HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<?> updateRequestsStatusUsersOwnEvents(@RequestBody EventRequestStatusUpdateRequest dto,
                                                                @PathVariable(name = "userId") long userId,
                                                                @PathVariable(name = "eventId") long eventId) {
        eventService.updateRequestsStatusUsersOwnEvents(dto, userId, eventId);
        return new ResponseEntity<>(eventService.getUpdatedRequestStatus(dto.getRequestIds(),userId, eventId),
                HttpStatus.OK);
    }
}
