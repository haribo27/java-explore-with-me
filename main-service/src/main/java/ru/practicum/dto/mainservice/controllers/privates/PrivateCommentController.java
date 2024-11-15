package ru.practicum.dto.mainservice.controllers.privates;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.dto.comment.CommentDto;
import ru.practicum.dto.mainservice.dto.comment.CommentRequestDto;
import ru.practicum.dto.mainservice.dto.comment.UpdateCommentDto;
import ru.practicum.dto.mainservice.service.CommentService;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping("/{userId}/events/{eventId}/comments")
    public ResponseEntity<CommentDto> saveComment(@RequestBody @Valid CommentRequestDto commentRequestDto,
                                                  @PathVariable long userId,
                                                  @PathVariable long eventId) {
        return new ResponseEntity<>(commentService.saveComment(commentRequestDto, userId, eventId),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/events/comments/{commentId}")
    public ResponseEntity<Void> userDeleteComment(@PathVariable long userId, @PathVariable long commentId) {
        commentService.userDeleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/events/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@RequestBody @Valid UpdateCommentDto request,
                                                    @PathVariable long userId,
                                                    @PathVariable long commentId) {
        return ResponseEntity.ok(commentService.updateComment(request, userId, commentId));
    }
}
