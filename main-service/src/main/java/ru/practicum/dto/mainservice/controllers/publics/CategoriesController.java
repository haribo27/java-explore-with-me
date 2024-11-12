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
import ru.practicum.dto.mainservice.dto.category.CategoryDto;
import ru.practicum.dto.mainservice.service.CategoryService;

import java.util.List;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoriesController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findCategoriesByParams(@RequestParam(value = "from", defaultValue = "0")
                                                                    @PositiveOrZero Integer from,
                                                                    @RequestParam(value = "size", defaultValue = "10")
                                                                    @Positive Integer size) {
        return ResponseEntity.ok(categoryService.findCategoriesByParams(from, size));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable long catId) {
        return ResponseEntity.ok(categoryService.getCategory(catId));
    }
}
