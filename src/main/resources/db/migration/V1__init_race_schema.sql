create table races (
    id                  uuid unique,
    version             bigint default 0,
    duration_in_seconds smallint,
    race_status         char(255),
    primary key (id)
);