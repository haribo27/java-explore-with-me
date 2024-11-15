package ru.practicum.dto.mainservice.service;

import ru.practicum.dto.mainservice.dto.comment.CommentDto;
import ru.practicum.dto.mainservice.dto.comment.CommentRequestDto;
import ru.practicum.dto.mainservice.dto.comment.UpdateCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto saveComment(CommentRequestDto requestDto, long userId, long eventId);

    CommentDto getComment(long commentId);

    void adminDeleteComment(long commentId);

    void userDeleteComment(long userId, long commentId);

    List<CommentDto> getEventComments(long eventId, Integer from, Integer size);

    CommentDto updateComment(UpdateCommentDto request, long userId, long commentId);
}
