package ru.practicum.dto.mainservice.controllers.privates;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.dto.mainservice.service.RequestService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PrivateRequestController {

    private final RequestService requestService;

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> addRequestToEvent(@PathVariable long userId,
                                                                     @RequestParam("eventId") long eventId) {
        return new ResponseEntity<>(requestService.addRequestToEvent(userId, eventId),
                HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getUsersRequests(@PathVariable long userId) {
        return ResponseEntity.ok(requestService.findUsersRequests(userId));
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable long userId,
                                                                 @PathVariable long requestId) {
        return ResponseEntity.ok(requestService.cancelRequest(userId, requestId));
    }

}
