package ru.practicum.dto.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.dto.mainservice.model.Location;

import java.time.LocalDateTime;

@Data
public class UpdateRequestEventDto {

    @Size(min = 20, max = 2000,
            message = "Длина краткого содержания события должна содержать от 20 до 2000 символлов")
    private String annotation;
    @PositiveOrZero(message = "Id категории не может быть меньше 0")
    private Long category;
    @Size(min = 20, max = 7000,
            message = "Длина описания события должна содержать от 20 до 2000 символлов")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "Дата и время события должны быть в будущем")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero(message = "Ограничения по участникам должно быть 0 или больше")
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    @Size(min = 3, max = 120,
            message = "Название события должно содержать от 3х до 120 символов.")
    private String title;
}
