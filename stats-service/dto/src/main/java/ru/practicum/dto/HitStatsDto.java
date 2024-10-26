package ru.practicum.dto;

import lombok.Data;

@Data
public class HitStatsDto {

    private String app;
    private String uri;
    private long hits;

    public HitStatsDto(String app, String uri, long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
