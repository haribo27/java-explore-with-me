package ru.practicum.dto.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.mainservice.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUserByIdIn(Set<Long> ids, Pageable pageable);

    Optional<User> findByEmail(String email);
}
