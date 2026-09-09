create table freeze_consistency (
    participation_id        bigint not null,
    freezed_at              timestamp,
    should_be_unfreezed_at  timestamp,
    primary key (participation_id)
);