package ru.practicum.dto.mainservice.controllers.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.dto.event.EventFullDto;
import ru.practicum.dto.mainservice.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.mainservice.model.EventState;
import ru.practicum.dto.mainservice.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@Validated
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> findEventsByParams(@RequestParam(value = "users", required = false) Set<Long> users,
                                                                 @RequestParam(value = "states", required = false) Set<EventState> states,
                                                                 @RequestParam(value = "categories", required = false) Set<Long> categories,
                                                                 @RequestParam(value = "rangeStart", required = false)
                                                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                                 @RequestParam(value = "rangeEnd", required = false)
                                                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                                 @RequestParam(value = "from", defaultValue = "0")
                                                                 @PositiveOrZero Integer from,
                                                                 @RequestParam(value = "size", defaultValue = "10")
                                                                 @Positive Integer size) {
        return ResponseEntity.ok(eventService.findEventsByParams(users, states, categories, rangeStart,
                rangeEnd, from, size));

    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable long eventId,
                                                    @RequestBody @Valid UpdateEventAdminRequest updateRequest) {
        return ResponseEntity.ok(eventService.adminUpdateEvent(updateRequest, eventId));

    }
}
