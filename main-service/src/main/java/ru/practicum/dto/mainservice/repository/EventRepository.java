package ru.practicum.dto.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.dto.mainservice.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {


    Set<Event> findEventByIdIn(Set<Long> ids);

    List<Event> findAllByInitiator_Id(long userId, Pageable pageable);

    Optional<Event> findEventByInitiator_IdAndId(long userId, long eventId);

    @Query("SELECT e " +
            "FROM Event as e " +
            "JOIN FETCH e.initiator " +
            "WHERE e.id = ?1 ")
    Optional<Event> findEventByIdWithInitiator(long eventId);
}
