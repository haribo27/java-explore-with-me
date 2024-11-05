package ru.practicum.dto.mainservice.controllers.publics;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.service.CompilationService;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationsController {

    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<?> findCompilations(@RequestParam(value = "pinned", required = false) Boolean pinned,
                                              @RequestParam(value = "from", defaultValue = "0") Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {

        return new ResponseEntity<>(compilationService.findByParams(pinned, from, size),
                HttpStatus.OK);
    }

    @GetMapping("/{comId}")
    public ResponseEntity<?> findCompilationById(@PathVariable long comId) {
        return new ResponseEntity<>(compilationService.findById(comId),
                HttpStatus.OK);
    }
}
