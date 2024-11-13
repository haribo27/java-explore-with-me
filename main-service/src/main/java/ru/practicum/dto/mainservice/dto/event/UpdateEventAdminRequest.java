package ru.practicum.dto.mainservice.dto.event;

import lombok.Getter;

@Getter
public class UpdateEventAdminRequest extends UpdateEventRequest {

    private StateAction stateAction;

    public enum StateAction {
        PUBLISH_EVENT,
        REJECT_EVENT
    }
}
