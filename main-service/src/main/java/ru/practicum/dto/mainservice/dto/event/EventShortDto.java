package ru.practicum.dto.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.dto.mainservice.dto.category.CategoryDto;
import ru.practicum.dto.mainservice.dto.comment.CommentDto;
import ru.practicum.dto.mainservice.dto.user.UserDto;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EventShortDto {

    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private long id;
    private UserDto initiator;
    private Boolean paid;
    private String title;
    private long views;
    private Set<CommentDto> comments;
}
