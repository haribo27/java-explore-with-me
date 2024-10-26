package ru.practicum.service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "hits")
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 50)
    private String app;
    @Column(nullable = false, length = 350)
    private String uri;
    @Column(nullable = false, length = 30)
    private String ip;
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
