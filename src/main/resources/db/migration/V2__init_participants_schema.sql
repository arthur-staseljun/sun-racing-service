create table participations (
    id              serial,
    race_id         uuid,
    participant_id  varchar(255),
    score           smallint,
    created_at      timestamp,
    updated_at      timestamp,
    primary key (id)
);