package ru.practicum.dto.mainservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 2000)
    private String annotation;
    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(nullable = false, length = 7000)
    private String description;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @Embedded
    private Location location;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @Column(nullable = false)
    private Boolean paid;
    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;
    @Column(nullable = false, length = 120)
    private String title;
    @Column(name = "confirmed_requests")
    private int confirmedRequests;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User initiator;
}
