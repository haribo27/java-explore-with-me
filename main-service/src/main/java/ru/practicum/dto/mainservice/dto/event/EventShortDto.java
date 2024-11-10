package ru.practicum.dto.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.dto.mainservice.model.Category;
import ru.practicum.dto.mainservice.model.User;

import java.time.LocalDateTime;

@Data
public class EventShortDto {

    private String annotation;
    private Category category;
    private Integer confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private long id;
    private User initiator;
    private Boolean paid;
    private String title;
    private long views;
}
