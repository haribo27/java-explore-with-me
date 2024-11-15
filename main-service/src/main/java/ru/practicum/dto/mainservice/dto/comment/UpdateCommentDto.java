package ru.practicum.dto.mainservice.dto.comment;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCommentDto {

    @Size(min = 1, max = 5000)
    private String text;

}
