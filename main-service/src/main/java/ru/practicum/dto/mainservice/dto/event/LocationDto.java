package ru.practicum.dto.mainservice.dto.event;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationDto {

    @NotNull
    @Min(value = -90)
    @Max(value = 90)
    private Double lat;
    @Min(value = -180)
    @Max(value = 180)
    @NotNull
    private Double lon;
}
