package ru.practicum.dto.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.mainservice.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u.* " +
            "FROM users as u " +
            "WHERE u.id IN ?1 " +
            "OFFSET ?2 " +
            "LIMIT ?3",
            nativeQuery = true)
    List<User> findUserWithIds(List<Long> ids,
                               int offset,
                               int limit);

    @Query(value = "SELECT u.* " +
            "FROM users as u " +
            "OFFSET ?1 " +
            "LIMIT ?2",
            nativeQuery = true)
    List<User> findUserBy(Integer from, Integer size);

    Optional<User> findByEmail(String email);
}
