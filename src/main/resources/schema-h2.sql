create table if not exists loan_applications_schema.status (
  id tinyint primary key not null,
  description varchar(200)
);

create table if not exists loan_applications_schema.loan_application (
  id bigint primary key auto_increment not null,
  request_uid varchar(32),
  personal_id bigint,
  amount decimal,
  name varchar(300),
  surname varchar(300),
  term varchar(500),
  status tinyint,
  timestamp timestamp,
  foreign key (status) references loan_applications_schema.status(id)
);

create table if not exists loan_applications_schema.blacklisted_persons (
  personal_id bigint,
  description varchar(300)
);