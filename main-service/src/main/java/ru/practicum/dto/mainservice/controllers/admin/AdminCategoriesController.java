package ru.practicum.dto.mainservice.controllers.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.dto.category.CategoryDto;
import ru.practicum.dto.mainservice.dto.category.RequestCategoryDto;
import ru.practicum.dto.mainservice.service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody @Valid RequestCategoryDto requestCategoryDto) {
        return new ResponseEntity<>(categoryService.saveCategory(requestCategoryDto),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<?> deleteCategory(@PathVariable long catId) {
        categoryService.deleteCategory(catId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable long catId,
                                                      @RequestBody @Valid RequestCategoryDto requestCategoryDto) {
        return ResponseEntity.ok(categoryService.updateCategory(catId, requestCategoryDto));
    }
}
