create table races (
    id                      uuid,
    duration_in_seconds     integer,
    race_status             text,
    created_at              timestamp,
    should_be_finished_at   timestamp,
    started_at              timestamp,
    finished_at             timestamp,
    updated_at              timestamp,
    primary key (id)
);