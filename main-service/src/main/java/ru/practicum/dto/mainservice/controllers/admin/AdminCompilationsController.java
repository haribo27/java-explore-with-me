package ru.practicum.dto.mainservice.controllers.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.dto.request.RequestCompilationDto;
import ru.practicum.dto.mainservice.dto.request.UpdateRequestCompilationDto;
import ru.practicum.dto.mainservice.service.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationsController {

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<?> saveCompilation(@RequestBody @Valid RequestCompilationDto compilationDto) {
        return new ResponseEntity<>(compilationService.saveCompilation(compilationDto),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{comId}")
    public ResponseEntity<?> updateCompilation(@RequestBody @Valid UpdateRequestCompilationDto compilationDto,
                                               @PathVariable long comId) {
        return new ResponseEntity<>(compilationService.updateCompilation(compilationDto, comId),
                HttpStatus.OK);
    }

    @DeleteMapping("/{comId}")
    public ResponseEntity<?> deleteCompilation(@PathVariable long comId) {
        compilationService.deleteCompilation(comId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
