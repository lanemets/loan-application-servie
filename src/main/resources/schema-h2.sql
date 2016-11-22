CREATE TABLE IF NOT EXISTS loan_applications_schema.loan_application (
  id bigint PRIMARY KEY auto_increment not null,
  personal_id bigint,
  amount decimal,
  name varchar(300),
  surname varchar(300),
  term varchar(500),
  timestamp timestamp
);
