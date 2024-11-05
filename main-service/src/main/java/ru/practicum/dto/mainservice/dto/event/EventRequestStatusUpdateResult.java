package ru.practicum.dto.mainservice.dto.event;

import lombok.Data;
import ru.practicum.dto.mainservice.dto.request.ParticipationRequestDto;

import java.util.List;

@Data
public class EventRequestStatusUpdateResult {

    List<ParticipationRequestDto> confirmedRequests;
    List<ParticipationRequestDto> rejectedRequests;
}
