create table races (
    id                  uuid not null unique,
    version             bigint default 0,
    duration_in_seconds integer,
    race_status         text,
    created_at          timestamp,
    started_at          timestamp,
    finished_at         timestamp,
    updated_at          timestamp,
    primary key (id)
);