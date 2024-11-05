package ru.practicum.dto.mainservice.service;

import ru.practicum.dto.mainservice.dto.category.CategoryDto;
import ru.practicum.dto.mainservice.dto.category.RequestCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto saveCategory(RequestCategoryDto requestCategoryDto);

    CategoryDto updateCategory(long catId, RequestCategoryDto requestCategoryDto);

    void deleteCategory(long catId);

    List<CategoryDto> findCategoriesByParams(Integer from, Integer size);

    CategoryDto getCategory(long catId);
}
