package ru.practicum.dto.mainservice.dto.event;

import lombok.Data;
import ru.practicum.dto.mainservice.dto.request.ParticipationRequestDto;

import java.util.Set;

@Data
public class EventRequestStatusUpdateResult {

    private Set<ParticipationRequestDto> confirmedRequests;
    private Set<ParticipationRequestDto> rejectedRequests;
}
