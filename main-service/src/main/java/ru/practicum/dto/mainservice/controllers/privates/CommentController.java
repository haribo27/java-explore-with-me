package ru.practicum.dto.mainservice.controllers.privates;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.dto.comment.CommentDto;
import ru.practicum.dto.mainservice.dto.comment.CommentRequestDto;
import ru.practicum.dto.mainservice.service.CommentService;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    public ResponseEntity<CommentDto> saveComment(@RequestBody @Valid CommentRequestDto commentRequestDto,
                                                  @PathVariable long userId,
                                                  @PathVariable long eventId) {
        return new ResponseEntity<>(commentService.saveComment(commentRequestDto, userId, eventId),
                HttpStatus.CREATED);
    }

    @GetMapping("/events/comments/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable long commentId) {
        return ResponseEntity.ok(commentService.getComment(commentId));
    }

    @DeleteMapping("/admin/events/comments/{commentId}")
    public ResponseEntity<Void> adminDeleteComment(@PathVariable long commentId) {
        commentService.adminDeleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{userId}/events/comments/{commentId}")
    public ResponseEntity<Void> userDeleteComment(@PathVariable long userId, @PathVariable long commentId) {
        commentService.userDeleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }
}
