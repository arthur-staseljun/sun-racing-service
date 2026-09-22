create table participations (
    id                      bigserial,
    race_id                 uuid,
    participant_id          varchar(255),
    score                   integer,
    freezed                 boolean default false,
    should_be_unfreezed_at  timestamp,
    created_at              timestamp,
    updated_at              timestamp,
    primary key (id)
);