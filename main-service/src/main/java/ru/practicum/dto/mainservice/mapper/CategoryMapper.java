package ru.practicum.dto.mainservice.mapper;

import org.mapstruct.*;
import ru.practicum.dto.mainservice.dto.category.CategoryDto;
import ru.practicum.dto.mainservice.dto.category.RequestCategoryDto;
import ru.practicum.dto.mainservice.model.Category;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    Category mapToCategory(RequestCategoryDto requestCategoryDto);

    CategoryDto mapToCategoryDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCategory(RequestCategoryDto requestDto, @MappingTarget Category category);
}
