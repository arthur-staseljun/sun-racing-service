create table race_consistency (
    id                      uuid not null unique,
    started_at              timestamp,
    should_be_finished_at   timestamp,
    primary key (id)
);