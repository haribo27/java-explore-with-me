package ru.practicum.service.service;

import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.HitStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitStatService {

    void saveEndpointHit(HitRequestDto request);

    List<HitStatsDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);
}
