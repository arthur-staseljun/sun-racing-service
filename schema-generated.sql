create table races (duration_in_seconds integer, id uuid not null, race_status varchar(255) check ((race_status in ('CREATED','ACTIVE','FINISHED'))), primary key (id));
