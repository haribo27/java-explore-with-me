package ru.practicum.dto.mainservice.controllers.publics;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.mainservice.dto.comment.CommentDto;
import ru.practicum.dto.mainservice.service.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
@Validated
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable long commentId) {
        return ResponseEntity.ok(commentService.getComment(commentId));
    }

    @GetMapping("/{eventId}/comments")
    public ResponseEntity<List<CommentDto>> findEventComments(@RequestParam(defaultValue = "0")
                                                              @PositiveOrZero Integer from,
                                                              @RequestParam(defaultValue = "10")
                                                              @Positive Integer size,
                                                              @PathVariable long eventId) {
        return ResponseEntity.ok(commentService.getEventComments(eventId, from, size));
    }
}
