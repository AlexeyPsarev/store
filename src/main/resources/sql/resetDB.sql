DROP TABLE users IF EXISTS;
DROP TABLE applications IF EXISTS;

CREATE TABLE users (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(40) UNIQUE NOT NULL,
  password CHAR(80) NOT NULL,
  full_name VARCHAR(60) NOT NULL
);

CREATE TABLE applications (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  app_name VARCHAR NOT NULL,
  pkg_name VARCHAR UNIQUE NOT NULL,
  picture_128 VARCHAR,
  picture_512 VARCHAR,
  category VARCHAR(20),
  downloads INTEGER NOT NULL,
  time_uploaded TIMESTAMP,
  description VARCHAR,
  author VARCHAR REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE
);
