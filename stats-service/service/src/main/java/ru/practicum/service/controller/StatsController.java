package ru.practicum.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.HitStatsDto;
import ru.practicum.service.service.HitStatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final HitStatService statsService;

    @PostMapping("/hit")
    public ResponseEntity<String> saveEndpointHit(@RequestBody @Valid HitRequestDto request) {
        statsService.saveEndpointHit(request);
        return new ResponseEntity<>(" Информация сохранена",
                HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<HitStatsDto>> getStats(@RequestParam(value = "start")
                                                      String start,
                                                      @RequestParam(value = "end")
                                                      String end,
                                                      @RequestParam(value = "uris", required = false) String[] uris,
                                                      @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        return ResponseEntity.ok(statsService.getStats(start, end, uris, unique));
    }
}