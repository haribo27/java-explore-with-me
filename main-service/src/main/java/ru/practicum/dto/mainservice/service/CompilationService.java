package ru.practicum.dto.mainservice.service;

import ru.practicum.dto.mainservice.dto.compilation.CompilationDto;
import ru.practicum.dto.mainservice.dto.request.RequestCompilationDto;
import ru.practicum.dto.mainservice.dto.request.UpdateRequestCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto saveCompilation(RequestCompilationDto compilationDto);

    CompilationDto updateCompilation(UpdateRequestCompilationDto updateDto, long comId);

    void deleteCompilation(long comId);

    CompilationDto findById(long comId);

    List<CompilationDto> findByParams(Boolean pinned, Integer from, Integer size);
}
