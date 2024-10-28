package ru.practicum.client.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.client.util.EncodeDate;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.HitStatsDto;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatsClient {

    private final RestTemplate restTemplate;

    @Value("${hit-stat-server}")
    private final String baseUrl;

    public void recordRequest(HitRequestDto recordDto) {
        restTemplate.postForEntity(baseUrl + "/hit", recordDto, Void.class);
    }

    public ResponseEntity<List<HitStatsDto>> getStats(String start, String end, String[] uris, Boolean unique) throws UnsupportedEncodingException {
        String hitStatUrl = baseUrl + "/stats";
        start = EncodeDate.encodeDate(start);
        end = EncodeDate.encodeDate(end);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(hitStatUrl)
                .queryParam("start", start)
                .queryParam("end", end);

        if (uris != null && uris.length > 0) {
            builder.queryParam("uris", (Object[]) uris);
        }
        if (unique != null) {
            builder.queryParam("unique", unique);
        }

        String url = builder.toUriString();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
    }
}
