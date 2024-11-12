package ru.practicum.dto.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.mainservice.util.StartAfterTwoHours;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@StartAfterTwoHours(dateMethod = "getEventDate")
public class RequestEventDto {

    @NotBlank(message = "Краткое описание события не может быть пустым")
    @Size(min = 20, max = 2000,
            message = "Длина краткого содержания события должна содержать от 20 до 2000 символлов")
    private String annotation;
    @NotNull(message = "Id категории не может быть null")
    @PositiveOrZero(message = "Id категории не может быть меньше 0")
    private Long category;
    @NotBlank(message = "Описание события не может быть пустым")
    @Size(min = 20, max = 7000,
            message = "Длина описания события должна содержать от 20 до 2000 символлов")
    private String description;
    @NotNull(message = "Дата и время начала события не может быть пустой")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "Дата и время события должны быть в будущем")
    private LocalDateTime eventDate;
    @NotNull(message = "Место встречи не может быть пустым")
    private LocationDto location;
    private boolean paid;
    @PositiveOrZero(message = "Ограничения по участникам должно быть 0 или больше")
    private int participantLimit;
    @Builder.Default
    private boolean requestModeration = true;
    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 3, max = 120,
            message = "Название события должно содержать от 3х до 120 символов.")
    private String title;
}
