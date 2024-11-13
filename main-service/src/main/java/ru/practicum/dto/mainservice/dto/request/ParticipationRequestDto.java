package ru.practicum.dto.mainservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.practicum.dto.mainservice.model.RequestState;

import java.time.LocalDateTime;

import static ru.practicum.dto.mainservice.model.RequestState.PENDING;

@Data
public class ParticipationRequestDto {

    private LocalDateTime created = LocalDateTime.now();
    @NotNull(message = "id события не может быть равно null")
    @Positive(message = "id события не может быть меньше 0")
    private Long event;
    private long id;
    @NotNull(message = "id участника не может быть равно null")
    @Positive(message = "id участника не может быть меньше 0")
    private Long requester;
    private RequestState status = PENDING;
}
