package ru.practicum.dto.mainservice.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestDto {

    @NotBlank(message = "Тело коментария не может быть пустым или null")
    @Size(min = 1, max = 5000)
    private String text;
}
