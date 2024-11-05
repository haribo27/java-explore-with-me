package ru.practicum.dto.mainservice.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationDto {

    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
}
