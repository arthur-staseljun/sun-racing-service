create table participations (
    race_id         uuid,
    participant_id  varchar(255),
    created_at      timestamp,
    primary key (race_id)
);