create table races (
    id                  uuid not null unique,
    version             bigint default 0,
    duration_in_seconds smallint,
    race_status         char(255),
    created_at          timestamp default current_timestamp,
    started_at          timestamp,
    finished_at         timestamp,
    primary key (id)
);