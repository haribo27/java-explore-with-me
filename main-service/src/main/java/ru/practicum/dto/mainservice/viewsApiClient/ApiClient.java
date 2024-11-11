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
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiClient {

    private final StatsClient statsClient;

    @Value("${spring.application.name}")
    private String appName;

    public void sendHitRequestToApi(HttpServletRequest request) {
        log.info("Create request hit to stats service");
        String ipClient = request.getRemoteAddr();
        String endPointPath = request.getRequestURI();
        HitRequestDto requestDto = new HitRequestDto();
        requestDto.setApp(appName);
        requestDto.setIp(ipClient);
        requestDto.setUri(endPointPath);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        requestDto.setTimestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
        log.info("Send request to api {}", requestDto);
        statsClient.recordRequest(requestDto);
    }

    public List<HitStatsDto> getEventViews(List<Long> ids) {
        List<String> eventPaths = ids.stream()
                .map(id -> "/events/" + id)
                .toList();
        return statsClient.getStats(LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0).toString(),
                LocalDateTime.of(2024, Month.DECEMBER, 31, 0, 0, 0).toString(), eventPaths, true).getBody();
    }
}
