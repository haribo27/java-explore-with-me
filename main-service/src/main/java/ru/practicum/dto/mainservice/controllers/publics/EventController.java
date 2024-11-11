package ru.practicum.dto.mainservice.controllers.publics;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<?> findEventsWithFilterAndParams(@RequestParam(value = "text", required = false) String text,
                                                           @RequestParam(value = "categories", required = false) List<Long> categories,
                                                           @RequestParam(value = "paid", required = false) Boolean paid,
                                                           @RequestParam(value = "rangeStart", required = false)
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                           @RequestParam(value = "rangeEnd", required = false)
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                           @RequestParam(value = "onlyAvailable", required = false) Boolean onlyAvailable,
                                                           @RequestParam(value = "sort", required = false) String sort,
                                                           @RequestParam(value = "from",defaultValue = "0", required = false) Integer from,
                                                           @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                           HttpServletRequest request) {
        return new ResponseEntity<>(eventService.findEventsByParamsAndFilter(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findEventById(@PathVariable long id,
                                           HttpServletRequest request) {
        return new ResponseEntity<>(eventService.findById(id, request),
                HttpStatus.OK);
    }
}
