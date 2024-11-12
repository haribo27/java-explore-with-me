package ru.practicum.dto.mainservice.dto.event;

import lombok.Getter;

@Getter
public class UpdateEventUserRequest extends UpdateEventRequest {

    private StateAction stateAction;

    public enum StateAction {
        SEND_TO_REVIEW,
        CANCEL_REVIEW
    }
}
