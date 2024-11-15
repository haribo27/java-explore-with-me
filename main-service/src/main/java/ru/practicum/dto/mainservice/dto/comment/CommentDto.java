package ru.practicum.dto.mainservice.dto.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private long id;
    private String text;
    private UserCommentDto author;
    private EventCommentDto event;
    private LocalDateTime created;
}
