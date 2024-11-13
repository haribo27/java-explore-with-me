package ru.practicum.dto.mainservice.dto.request;

import lombok.Data;
import ru.practicum.dto.mainservice.model.RequestState;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;
    private RequestState status;
}
