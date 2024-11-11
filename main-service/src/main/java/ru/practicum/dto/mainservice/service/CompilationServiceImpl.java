package ru.practicum.dto.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.mainservice.dto.compilation.CompilationDto;
import ru.practicum.dto.mainservice.dto.request.RequestCompilationDto;
import ru.practicum.dto.mainservice.dto.request.UpdateRequestCompilationDto;
import ru.practicum.dto.mainservice.exception.EntityNotFoundException;
import ru.practicum.dto.mainservice.mapper.CompilationMapper;
import ru.practicum.dto.mainservice.model.Compilation;
import ru.practicum.dto.mainservice.model.Event;
import ru.practicum.dto.mainservice.repository.CompilationRepository;
import ru.practicum.dto.mainservice.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Override
    @Transactional
    public CompilationDto saveCompilation(RequestCompilationDto compilationDto) {
        log.info("Saving compilation {}", compilationDto);
        Compilation compilation = compilationMapper.mapToCompilation(compilationDto);
        log.info("Mapping event ids: {} to events", compilationDto.getEvents());
        if (compilationDto.getEvents() != null && !compilationDto.getEvents().isEmpty()) {
            List<Event> events = collectEventsFromIds(compilationDto.getEvents());
            log.info("Set events to compilation {}", events);
            compilation.setEvents(events);
        } else {
            compilation.setEvents(new ArrayList<>());
        }
        compilation = compilationRepository.save(compilation);
        log.info("Compilation saved: {}", compilation);
        return compilationMapper.mapToDto(compilation);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(UpdateRequestCompilationDto updateDto, long comId) {
        log.info("Updating compilation: {}", comId);
        Compilation compilation = compilationRepository.findById(comId)
                .orElseThrow(() -> new EntityNotFoundException("Compilation with id=" + comId + " not found"));
        log.info("Updating fields: {}, {}", updateDto.isPinned(), updateDto.getTitle());
        compilationMapper.updateCompilation(updateDto, compilation);
        if (updateDto.getEvents() != null && !updateDto.getEvents().isEmpty()) {
            log.info("Updating events with ids: {}", updateDto.getEvents());
            compilation.setEvents(collectEventsFromIds(updateDto.getEvents()));
        }
        compilation = compilationRepository.save(compilation);
        log.info("Compilation saves: {}", compilation);
        return compilationMapper.mapToDto(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(long comId) {
        log.info("Deleting compilation with id: {}", comId);
        Compilation compilation = compilationRepository.findById(comId)
                .orElseThrow(() -> new EntityNotFoundException("Compilation with id=" + comId + " not found"));
        compilationRepository.delete(compilation);
        log.info("Compilation with id: {} deleted success", comId);
    }

    @Override
    public CompilationDto findById(long comId) {
        log.info("Finding compilation with id: {}", comId);
        return compilationRepository.findById(comId)
                .map(compilationMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Compilation with id=" + comId + " not found"));
    }

    @Override
    public List<CompilationDto> findByParams(Boolean pinned, Integer from, Integer size) {
        log.info("Find compilations by params: pinned: {}, from: {}, size: {}", pinned, from, size);
        return compilationRepository.findCompilationsBy(pinned, from, size)
                .stream()
                .map(compilationMapper::mapToDto)
                .toList();
    }

    private List<Event> collectEventsFromIds(List<Long> ids) {
        log.info("Find events from input ids: {} to compilation", ids);
        return ids.stream()
                .map(id -> eventRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Event with id=" + id + " not found")))
                .collect(Collectors.toList());

    }
}
