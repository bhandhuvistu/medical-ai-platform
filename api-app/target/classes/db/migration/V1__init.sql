-- V1__init.sql
create table if not exists job (
  id uuid primary key,
  patient_id varchar(100),
  status varchar(20) not null,
  created_at timestamp not null,
  updated_at timestamp not null
);

create table if not exists job_file (
  id uuid primary key,
  job_id uuid references job(id) on delete cascade,
  s3_key varchar(512) not null,
  content_type varchar(100),
  created_at timestamp not null
);

create table if not exists job_result (
  job_id uuid primary key references job(id) on delete cascade,
  json text,
  created_at timestamp not null
);
