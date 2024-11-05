package ru.practicum.dto.mainservice.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Location {
    private Double lat;
    private Double lon;
}