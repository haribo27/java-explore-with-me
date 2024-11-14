package ru.practicum.dto.mainservice.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.mainservice.dto.comment.CommentDto;
import ru.practicum.dto.mainservice.dto.comment.CommentRequestDto;
import ru.practicum.dto.mainservice.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment mapToComment(CommentRequestDto requestDto);

    CommentDto mapToDto(Comment comment);
}
