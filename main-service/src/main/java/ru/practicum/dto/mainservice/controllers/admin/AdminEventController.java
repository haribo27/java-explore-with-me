package ru.practicum.dto.mainservice.controllers.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.mainservice.model.EventState;
import ru.practicum.dto.mainservice.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<?> findEventsByParams(@RequestParam(value = "users", required = false) List<Long> users,
                                                @RequestParam(value = "states", required = false) List<EventState> states,
                                                @RequestParam(value = "categories", required = false) List<Long> categories,
                                                @RequestParam(value = "rangeStart", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                @RequestParam(value = "rangeEnd", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        System.out.println("kek");
        return new ResponseEntity<>(eventService.findEventsByParams(users, states, categories,
                rangeStart, rangeEnd, from, size),
                HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable long eventId,
                                         @RequestBody @Valid UpdateEventAdminRequest updateRequest) {
        return new ResponseEntity<>(eventService.adminUpdateEvent(updateRequest, eventId),
                HttpStatus.OK);
    }
}
