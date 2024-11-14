package ru.practicum.dto.mainservice.service;

import ru.practicum.dto.mainservice.dto.comment.CommentDto;
import ru.practicum.dto.mainservice.dto.comment.CommentRequestDto;

import java.util.List;
import java.util.Set;

public interface CommentService {

    CommentDto saveComment(CommentRequestDto requestDto, long userId, long eventId);

    CommentDto getComment(long commentId);

    void adminDeleteComment(long commentId);

    void userDeleteComment(long userId, long commentId);

    Set<CommentDto> getEventComments(List<Long> eventsIds);
}
