create table races_joined (
    id                bigserial,
    race_id           uuid,
    participant_id    varchar(255),
    primary key (participant_id)
);