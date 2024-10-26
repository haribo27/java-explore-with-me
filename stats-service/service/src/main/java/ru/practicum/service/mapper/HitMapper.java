package ru.practicum.service.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.service.model.Hit;

@Mapper(componentModel = "spring")
public interface HitMapper {

    Hit mapToHit(HitRequestDto request);
}
