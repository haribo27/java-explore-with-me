package ru.practicum.dto.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.mainservice.model.Request;
import ru.practicum.dto.mainservice.model.RequestState;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {


    Optional<Request> findByRequesterIdAndEvent_Id(long userId, long eventId);

    @Query("SELECT COUNT(*) " +
            "FROM Request as r " +
            "WHERE r.event.id = ?1 ")
    Long getPartitionsRequestToEvent(long eventId);

    @Query("SELECT r " +
            "FROM Request as r " +
            "WHERE r.requester.id = ?1 ")
    List<Request> findUsersRequests(long userId);

    Optional<Request> findByIdAndRequester_Id(long id, long userId);

    @Query("SELECT r " +
            "FROM Request r " +
            "JOIN Event e ON r.event.id = e.id " +
            "WHERE e.initiator.id = ?1 " +
            "AND e.id = ?2 ")
    List<Request> getOwnersEventRequests(long userId, long eventId);


    @Modifying
    @Query("UPDATE Request r " +
            "SET r.status = ?2 " +
            "WHERE r.id IN ?1")
    void updateRequestStatus(@Param("requestIds") List<Long> requestIds,
                            @Param("status") RequestState status);

    List<Request> findRequestByIdIn(List<Long> ids);
}
