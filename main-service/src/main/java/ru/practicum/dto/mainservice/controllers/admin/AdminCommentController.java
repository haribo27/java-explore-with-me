package ru.practicum.dto.mainservice.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.dto.mainservice.service.CommentService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService commentService;

    @DeleteMapping("/events/comments/{commentId}")
    public ResponseEntity<Void> adminDeleteComment(@PathVariable long commentId) {
        commentService.adminDeleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
