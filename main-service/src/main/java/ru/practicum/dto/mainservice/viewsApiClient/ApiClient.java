package ru.practicum.dto.mainservice.viewsApiClient;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.client.client.StatsClient;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.HitStatsDto;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiClient {

    private final StatsClient statsClient;

    @Value("${spring.application.name}")
    private String appName;

    public void sendHitRequestToApi(HttpServletRequest request) {
        String ipClient = request.getRemoteAddr();
        String endPointPath = request.getRequestURI();
        statsClient.recordRequest(new HitRequestDto(appName, endPointPath, ipClient, LocalDateTime.now()));
    }

    public List<HitStatsDto> getEventViews(List<Long> ids) {
        List<String> eventPaths = ids.stream()
                .map(id -> "/events/" + id)
                .toList();
        return statsClient.getStats(LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0).toString(),
                LocalDateTime.of(2024, Month.DECEMBER, 31, 0, 0).toString(), eventPaths, true).getBody();
    }
}
