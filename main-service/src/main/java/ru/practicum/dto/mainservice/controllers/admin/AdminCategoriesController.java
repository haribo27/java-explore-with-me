package ru.practicum.dto.mainservice.controllers.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mainservice.dto.category.RequestCategoryDto;
import ru.practicum.dto.mainservice.service.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> saveCategory(@RequestBody @Valid RequestCategoryDto requestCategoryDto) {
        return new ResponseEntity<>(categoryService.saveCategory(requestCategoryDto),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<?> deleteCategory(@PathVariable long catId) {
        categoryService.deleteCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<?> updateCategory(@PathVariable long catId,
                                            @RequestBody @Valid RequestCategoryDto requestCategoryDto) {
        return new ResponseEntity<>(categoryService.updateCategory(catId, requestCategoryDto),
                HttpStatus.OK);
    }
}
