package ru.practicum.dto.mainservice.controllers.privates;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.service.RequestService;

@RestController
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<?> addRequestToEvent(@PathVariable long userId,
                                               @RequestParam("eventId") long eventId) {
        return new ResponseEntity<>(requestService.addRequestToEvent(userId, eventId),
                HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/requests")
    public ResponseEntity<?> getUsersRequests(@PathVariable long userId) {
        return new ResponseEntity<>(requestService.findUsersRequests(userId),
                HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<?> cancelRequest(@PathVariable long userId,
                                           @PathVariable long requestId) {
        return new ResponseEntity<>(requestService.cancelRequest(userId,requestId),
                HttpStatus.OK);
    }

}
