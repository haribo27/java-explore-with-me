package ru.practicum.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.HitStatsDto;
import ru.practicum.service.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitStatRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT new ru.practicum.dto.HitStatsDto(h.app,h.uri, " +
            "(CASE WHEN :unique = true THEN COUNT(DISTINCT h.ip) " +
            "ELSE COUNT(h.ip) END)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY h.uri")
    List<HitStatsDto> getStatsWithoutUris(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("unique") Boolean unique);

    @Query("SELECT new ru.practicum.dto.HitStatsDto(h.app,h.uri, " +
            "(CASE WHEN :unique = true THEN COUNT(DISTINCT h.ip) " +
            "ELSE COUNT(h.ip) END)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY h.uri")
    List<HitStatsDto> getStatsWithUris(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end,
                                       @Param("uris") List<String> uris,
                                       @Param("unique") Boolean unique);


}
