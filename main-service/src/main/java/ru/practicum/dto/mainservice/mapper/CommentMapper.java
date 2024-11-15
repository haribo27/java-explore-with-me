package ru.practicum.dto.mainservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.dto.mainservice.dto.comment.CommentDto;
import ru.practicum.dto.mainservice.dto.comment.CommentRequestDto;
import ru.practicum.dto.mainservice.dto.comment.UpdateCommentDto;
import ru.practicum.dto.mainservice.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment mapToComment(CommentRequestDto requestDto);

    CommentDto mapToDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateComment(UpdateCommentDto request, @MappingTarget Comment comment);
}
