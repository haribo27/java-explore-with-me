package ru.practicum.dto.mainservice.mapper;

import org.mapstruct.*;
import ru.practicum.dto.mainservice.dto.compilation.CompilationDto;
import ru.practicum.dto.mainservice.dto.request.RequestCompilationDto;
import ru.practicum.dto.mainservice.dto.request.UpdateRequestCompilationDto;
import ru.practicum.dto.mainservice.model.Compilation;


@Mapper(componentModel = "spring")
public interface CompilationMapper {

    @Mapping(target = "events", ignore = true)
    Compilation mapToCompilation(RequestCompilationDto compilationDto);

    CompilationDto mapToDto(Compilation compilation);

    @Mapping(target = "events", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCompilation(UpdateRequestCompilationDto requestDto, @MappingTarget Compilation compilation);
}
