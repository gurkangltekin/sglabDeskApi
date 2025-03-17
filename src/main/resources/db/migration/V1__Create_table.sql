CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE Users (
   id UUID ,
   is_deleted BOOLEAN NOT NULL,
   version TIMESTAMP,
   username VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   name VARCHAR(255) NOT NULL,
   password VARCHAR(255),
   create_date TIMESTAMP,
   email_verified_date TIMESTAMP,
   remember_token VARCHAR(1000),
   access_token varchar(256),
   avatar varchar(1000),
   description varchar(1000),
   expire_date TIMESTAMP,
   fcmtoken varchar(3000),
   online BOOLEAN,
   open_id varchar(255),
   token varchar(1000),
   type int,
   is_enabled boolean,
    role varchar(10),
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS confirmation_token
(
    id           UUID ,
    version timestamp,
    is_deleted BOOLEAN,
    token        varchar(255)       not null,
    expires_at   TIMESTAMP,
    confirmed_at TIMESTAMP,
    user_id      uuid,
    create_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_user_id
                              FOREIGN KEY (user_id)

    REFERENCES users (id) on delete cascade
    );

CREATE TABLE mails (
                       id UUID NOT NULL,
                       is_deleted BOOLEAN NOT NULL,
                       version TIMESTAMP,
                       create_date TIMESTAMP,
                       subject TEXT,
                       body TEXT,
                       mail_address TEXT,
                       is_send BOOLEAN,
                       send_date TIMESTAMP,
                       detail TEXT
);

CREATE TABLE IF NOT EXISTS authentication_token
(
    id           UUID,
    version timestamp,
    is_deleted BOOLEAN,
    token        varchar(255)       not null,
    expires_at   TIMESTAMP,
    confirmed_at TIMESTAMP,
    user_id      uuid,
    create_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_user_id
                              FOREIGN KEY (user_id)

    REFERENCES users (id) on delete cascade
    );