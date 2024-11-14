package ru.practicum.dto.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.mainservice.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
