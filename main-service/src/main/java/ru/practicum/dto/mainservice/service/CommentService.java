package ru.practicum.dto.mainservice.service;

import ru.practicum.dto.mainservice.dto.comment.CommentDto;
import ru.practicum.dto.mainservice.dto.comment.CommentRequestDto;

public interface CommentService {

    CommentDto saveComment(CommentRequestDto requestDto, long userId, long eventId);

    CommentDto getComment(long commentId);

    void adminDeleteComment(long commentId);

    void userDeleteComment(long userId, long commentId);
}
