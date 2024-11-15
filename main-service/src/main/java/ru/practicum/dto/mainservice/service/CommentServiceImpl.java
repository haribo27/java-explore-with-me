package ru.practicum.dto.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.mainservice.dto.comment.CommentDto;
import ru.practicum.dto.mainservice.dto.comment.CommentRequestDto;
import ru.practicum.dto.mainservice.dto.comment.UpdateCommentDto;
import ru.practicum.dto.mainservice.exception.ConditionsAreNotMet;
import ru.practicum.dto.mainservice.exception.EntityNotFoundException;
import ru.practicum.dto.mainservice.mapper.CommentMapper;
import ru.practicum.dto.mainservice.model.Comment;
import ru.practicum.dto.mainservice.model.Event;
import ru.practicum.dto.mainservice.model.User;
import ru.practicum.dto.mainservice.repository.CommentRepository;
import ru.practicum.dto.mainservice.repository.EventRepository;
import ru.practicum.dto.mainservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto saveComment(CommentRequestDto requestDto, long userId, long eventId) {
        log.info("Save a new comment: {}, from user: {}, to event: {}", requestDto, userId, eventId);
        Comment comment = commentMapper.mapToComment(requestDto);
        comment.setCreated(LocalDateTime.now());
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + userId + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " not found"));
        comment.setAuthor(author);
        log.info("Set author: {} to comment", author);
        comment.setEvent(event);
        log.info("Set event: {} to comment", event);
        comment = commentRepository.save(comment);
        log.info("Comment saved {}", comment);
        return commentMapper.mapToDto(comment);
    }

    @Override
    public CommentDto getComment(long commentId) {
        log.info("Getting comment with id: {}", commentId);
        return commentRepository.findById(commentId)
                .map(commentMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id=" + commentId + "not found"));
    }

    @Override
    @Transactional
    public void adminDeleteComment(long commentId) {
        log.info("Deleting comment from admin");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id=" + commentId + "not found"));
        deleteComment(comment);
    }

    @Override
    @Transactional
    public void userDeleteComment(long userId, long commentId) {
        log.info("Deleting comment from author");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + userId + " not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id=" + commentId + "not found"));
        if (comment.getAuthor().getId() != user.getId()) {
            log.warn("User is not author of comment");
            throw new ConditionsAreNotMet("You don't have rights to delete comment." +
                    " Only author of comment can delete.");
        }
        deleteComment(comment);
    }

    @Override
    public List<CommentDto> getEventComments(long eventId, Integer from, Integer size) {
        log.info("Getting event: {} comments", eventId);
        Pageable pageable = PageRequest.of(from, size);
        return commentRepository.findAllByEventId(eventId, pageable)
                .stream()
                .map(commentMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentDto updateComment(UpdateCommentDto request, long userId, long commentId) {
        log.info("Updating comment id: {} request body: {}", commentId, request);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id=" + commentId + "not found"));
        if (comment.getCreated().plusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConditionsAreNotMet("You can't update comment after 2 hours from create comment");
        }
        if (comment.getAuthor().getId() != userId) {
            throw new ConditionsAreNotMet("Only author can update comment");
        }
        commentMapper.updateComment(request, comment);
        comment = commentRepository.save(comment);
        log.info("Comment updated and saved: {}", comment);
        return commentMapper.mapToDto(comment);
    }

    private void deleteComment(Comment comment) {
        commentRepository.delete(comment);
        log.info("Comment: {} deleted", comment);
    }
}
