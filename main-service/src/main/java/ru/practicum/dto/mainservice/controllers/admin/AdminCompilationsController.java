package ru.practicum.dto.mainservice.controllers.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.dto.compilation.CompilationDto;
import ru.practicum.dto.mainservice.dto.request.RequestCompilationDto;
import ru.practicum.dto.mainservice.dto.request.UpdateRequestCompilationDto;
import ru.practicum.dto.mainservice.service.CompilationService;

@Controller
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationsController {

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> saveCompilation(@RequestBody @Valid RequestCompilationDto compilationDto) {
        return new ResponseEntity<>(compilationService.saveCompilation(compilationDto),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{comId}")
    public ResponseEntity<CompilationDto> updateCompilation(@RequestBody @Valid UpdateRequestCompilationDto compilationDto,
                                                            @PathVariable long comId) {
        return ResponseEntity.ok(compilationService.updateCompilation(compilationDto, comId));
    }

    @DeleteMapping("/{comId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable long comId) {
        compilationService.deleteCompilation(comId);
        return ResponseEntity.noContent().build();
    }
}
