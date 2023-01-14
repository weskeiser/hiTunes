/* drop table  assistant cascade; */
/* drop table  "power" cascade; */
/* drop table  superhero cascade; */

CREATE TABLE IF NOT EXISTS superhero (
  superhero_id serial PRIMARY KEY,
  name varchar(50) NOT NULL,
  alias varchar(30),
  origin varchar(30)
);


CREATE TABLE IF NOT EXISTS assistant (
  assistant_id serial PRIMARY KEY,
  name varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS "power" (
  power_id serial PRIMARY KEY,
  name varchar(30) NOT NULL,
  description varchar(240) NOT NULL
);
