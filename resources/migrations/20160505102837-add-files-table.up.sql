CREATE TABLE files
(owner VARCHAR(20) NOT NULL,
 type VARCHAR(20) NOT NULL,
 name VARCHAR(50) NOT NULL,
 data BYTEA,
 PRIMARY KEY (owner, name));
