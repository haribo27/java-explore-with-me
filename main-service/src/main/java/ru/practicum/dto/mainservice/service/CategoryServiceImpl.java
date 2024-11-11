package ru.practicum.dto.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.mainservice.dto.category.CategoryDto;
import ru.practicum.dto.mainservice.dto.category.RequestCategoryDto;
import ru.practicum.dto.mainservice.exception.ConditionsAreNotMet;
import ru.practicum.dto.mainservice.exception.EntityNotFoundException;
import ru.practicum.dto.mainservice.mapper.CategoryMapper;
import ru.practicum.dto.mainservice.model.Category;
import ru.practicum.dto.mainservice.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto saveCategory(RequestCategoryDto requestCategoryDto) {
        log.info("Saving new category: {}", requestCategoryDto);
        isCategoryExists(requestCategoryDto);
        Category category = categoryMapper.mapToCategory(requestCategoryDto);
        category = categoryRepository.save(category);
        log.info("Category saved success");
        return categoryMapper.mapToCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(long catId, RequestCategoryDto requestCategoryDto) {
        log.info("Updating category with id: {}, updated category: {}", catId, requestCategoryDto);
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Category with id=" + catId + " was not found"));
        categoryMapper.updateCategory(requestCategoryDto, category);
        category = categoryRepository.save(category);
        log.info("Category {} updated", catId);
        return categoryMapper.mapToCategoryDto(category);
    }

    private void isCategoryExists(RequestCategoryDto requestCategoryDto) {
        log.info("Check is category: {} exists", requestCategoryDto.getName());
        Optional<Category> isCategoryExists = categoryRepository.findByName(requestCategoryDto.getName());
        if (isCategoryExists.isPresent()) {
            log.warn("This category {} is already exists", requestCategoryDto.getName());
            throw new ConditionsAreNotMet("Integrity constraint has been violated.");
        }
    }

    @Override
    @Transactional
    public void deleteCategory(long catId) {
        log.info("Deleting category with id: {}", catId);
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Category with id=" + catId + " was not found"));
        categoryRepository.delete(category);
        log.info("Category with id: {} deleted", catId);
    }

    @Override
    public List<CategoryDto> findCategoriesByParams(Integer from, Integer size) {
        log.info("Finding categories by params: from: {}, size: {}", from, size);
        return categoryRepository.findCategoryBy(from, size)
                .stream()
                .map(categoryMapper::mapToCategoryDto)
                .toList();
    }

    @Override
    public CategoryDto getCategory(long catId) {
        return categoryRepository.findById(catId)
                .map(categoryMapper::mapToCategoryDto)
                .orElseThrow(() -> new EntityNotFoundException("Category with id=" + catId + " was not found"));
    }
}
