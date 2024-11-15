package ru.practicum.dto.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.mainservice.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT e.id AS id, COUNT(c.id) AS commentCount " +
            "FROM events AS e " +
            "LEFT JOIN comments AS c ON e.id = c.event_id " +
            "WHERE e.id IN (:eventIds) " +
            "GROUP BY e.id " +
            "ORDER BY commentCount DESC", nativeQuery = true)
    List<EventCountCommentProjection> countEventsComments(@Param("eventIds") List<Long> eventIds);

    List<Comment> findAllByEventId(long eventId, Pageable pageable);
}
