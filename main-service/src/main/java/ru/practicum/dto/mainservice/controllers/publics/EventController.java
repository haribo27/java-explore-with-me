package ru.practicum.dto.mainservice.controllers.publics;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.mainservice.dto.event.EventFullDto;
import ru.practicum.dto.mainservice.dto.event.EventShortDto;
import ru.practicum.dto.mainservice.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> findEventsWithFilterAndParams(@RequestParam(value = "text", required = false) String text,
                                                                             @RequestParam(value = "categories", required = false) Set<Long> categories,
                                                                             @RequestParam(value = "paid", required = false) Boolean paid,
                                                                             @RequestParam(value = "rangeStart", required = false)
                                                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                                             @RequestParam(value = "rangeEnd", required = false)
                                                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                                             @RequestParam(value = "onlyAvailable", required = false) Boolean onlyAvailable,
                                                                             @RequestParam(value = "sort", required = false) String sort,
                                                                             @RequestParam(value = "from", defaultValue = "0", required = false)
                                                                             @PositiveOrZero Integer from,
                                                                             @RequestParam(value = "size", defaultValue = "10", required = false)
                                                                             @Positive Integer size,
                                                                             HttpServletRequest request) {
        return ResponseEntity.ok(eventService.findEventsByParamsAndFilter(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> findEventById(@PathVariable long id,
                                                      HttpServletRequest request) {
        return ResponseEntity.ok(eventService.findById(id, request));
    }
}
