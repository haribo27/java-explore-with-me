CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(100) UNIQUE                     NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email VARCHAR(254) UNIQUE                     NOT NULL,
    name  VARCHAR(250)                            NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title              VARCHAR(120)                            NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    user_id            BIGINT                                  NOT NULL,
    event_date         TIMESTAMP                               NOT NULL,
    state              varchar(255) check (state in ('PENDING', 'PUBLISHED', 'CANCELED')),
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  INT                                     NOT NULL,
    request_moderation BOOLEAN                                 NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    confirmed_requests INT,
    created_on         TIMESTAMP                               NOT NULL,
    published_on       TIMESTAMP,
    lat                DOUBLE PRECISION                        NOT NULL,
    lon                DOUBLE PRECISION                        NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT ref_category_fk FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT ref_user_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP                               NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    status       varchar(50) check (requests.status in ('PENDING', 'CONFIRMED', 'CANCELED', 'REJECTED')),

    PRIMARY KEY (id),
    CONSTRAINT ref_user_fk FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT ref_event_id FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    title  VARCHAR(50)                             NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation_events
(
    compilation_id BIGINT NOT NULL ,
    event_id BIGINT NOT NULL ,
    CONSTRAINT ref_compilation_fk FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    CONSTRAINT ref_event_id FOREIGN KEY (event_id) REFERENCES events (id)
);