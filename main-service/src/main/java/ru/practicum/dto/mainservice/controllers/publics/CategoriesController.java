package ru.practicum.dto.mainservice.controllers.publics;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.service.CategoryService;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> findCategoriesByParams(@RequestParam(value = "from", defaultValue = "0") Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return new ResponseEntity<>(categoryService.findCategoriesByParams(from,size),
                HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<?> getCategory(@PathVariable long catId) {
        return new ResponseEntity<>(categoryService.getCategory(catId),
                HttpStatus.OK);
    }
}
