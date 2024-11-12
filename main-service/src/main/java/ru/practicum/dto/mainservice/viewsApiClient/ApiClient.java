package ru.practicum.dto.mainservice.viewsApiClient;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.client.client.StatsClient;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.HitStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiClient {

    private final StatsClient statsClient;

    @Value("${spring.application.name}")
    private String appName;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void sendHitRequestToApi(HttpServletRequest request) {
        log.info("Create request hit to stats service");
        HitRequestDto requestDto = new HitRequestDto();
        requestDto.setApp(appName);
        requestDto.setIp(request.getRemoteAddr());
        requestDto.setUri(request.getRequestURI());
        requestDto.setTimestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
        log.info("Send request to api {}", requestDto);
        statsClient.recordRequest(requestDto);
    }

    public List<HitStatsDto> getEventViews(List<Long> ids, LocalDateTime earliestEventDate) {
        List<String> eventPaths = ids.stream()
                .map(id -> "/events/" + id)
                .toList();
        if (earliestEventDate == null) {
            return null;
        }
        return statsClient.getStats(
                earliestEventDate.format(formatter),
                LocalDateTime.now().format(formatter),
                eventPaths,
                true
        ).getBody();
    }
}
