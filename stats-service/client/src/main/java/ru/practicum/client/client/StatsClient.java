package ru.practicum.client.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.HitStatsDto;


import java.util.List;

@Component
@RequiredArgsConstructor
public class StatsClient {

    private final RestTemplate restTemplate;

    public void recordRequest(HitRequestDto recordDto) {
        String hitUrl = "http://localhost:9090/hit";
        restTemplate.postForEntity(hitUrl, recordDto, Void.class);
    }

    public ResponseEntity<List<HitStatsDto>> getStats(String start, String end, String[] uris, Boolean unique) {
        String baseUrl = "http://localhost:9090/stats";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("start", start)
                .queryParam("end", end);

        if (uris != null && uris.length > 0) {
            builder.queryParam("uris", (Object[]) uris); // Передаем массив как varargs
        }
        if (unique != null) {
            builder.queryParam("unique", unique);
        }

        String url = builder.toUriString();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<HitStatsDto>>() {
                }
        );
    }
}
