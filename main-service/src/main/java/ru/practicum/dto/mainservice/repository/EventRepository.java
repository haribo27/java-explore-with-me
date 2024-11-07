package ru.practicum.dto.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.dto.mainservice.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query(value = "SELECT e.* " +
            "FROM events AS e " +
            "WHERE e.user_id = ?1 " +
            "ORDER BY e.id " +
            "LIMIT ?3 OFFSET ?2",
            nativeQuery = true)
    List<Event> findEvents(long userId, Integer from, Integer size);

    Optional<Event> findEventByInitiator_IdAndId(long userId, long eventId);

    @Query("SELECT e " +
            "FROM Event as e " +
            "JOIN FETCH e.initiator " +
            "WHERE e.id = ?1 ")
    Optional<Event> findEventByIdWithInitiator(long eventId);

    @Modifying
    @Query(value = "UPDATE events " +
            "SET confirmed_requests = (" +
            "    SELECT COUNT(*) " +
            "    FROM requests " +
            "    WHERE event_id = events.id " +
            "    AND status = 'CONFIRMED'" +
            ")" +
            "WHERE id = :eventId", nativeQuery = true)
    void updateConfirmedRequests(long eventId);
}
