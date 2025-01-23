CREATE TABLE t_spotify_playlist
(
    id          SERIAL PRIMARY KEY NOT NULL,
    playlist_id VARCHAR(50),
    sun         VARCHAR(50),
    moon        VARCHAR(50),
    created_at  TIMESTAMP          NOT NULL,
    created_by  VARCHAR(50)        NOT NULL,
    updated_at  TIMESTAMP          NOT NULL,
    updated_by  VARCHAR(50)        NOT NULL,
    CONSTRAINT uq_playlist UNIQUE (playlist_id, sun, moon)
);