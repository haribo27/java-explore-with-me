package ru.practicum.dto.mainservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.dto.mainservice.model.RequestState;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    @NotNull
    private List<Long> requestIds;
    @NotNull
    private RequestState status;
}
