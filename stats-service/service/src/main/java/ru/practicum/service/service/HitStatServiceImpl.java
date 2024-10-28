package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.HitStatsDto;
import ru.practicum.service.exception.IncorrectDateParam;
import ru.practicum.service.mapper.HitMapper;
import ru.practicum.service.model.Hit;
import ru.practicum.service.repository.HitStatRepository;
import ru.practicum.service.util.DecodeDate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HitStatServiceImpl implements HitStatService {

    private final HitStatRepository hitStatRepository;
    private final HitMapper mapper;

    @Override
    @Transactional
    public void saveEndpointHit(HitRequestDto request) {
        log.info("Saving user hit");
        Hit hit = mapper.mapToHit(request);
        hitStatRepository.save(hit);
    }

    @Override
    public List<HitStatsDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        log.info("Get hit statistics with params: start: {}, end: {}, uris: {}, unique: {}",
                start, end, uris, unique);
        if (end.isBefore(start)) {
            log.info("Check that start time must be equal endDate or == startDate");
            throw new IncorrectDateParam("Start time must be before endtime");
        }
        log.info("Decoding start and end time");
        start = LocalDateTime.parse(DecodeDate.decodeDate(start));
        end = LocalDateTime.parse(DecodeDate.decodeDate(end));

        if (uris == null || uris.length == 0) {
            log.info("Getting hit statistic without uris");
            return hitStatRepository.getStatsWithoutUris(start, end, unique);
        }
        log.info("Getting hit statistics with uris");
        return hitStatRepository.getStatsWithUris(start, end, List.of(uris), unique);
    }
}
