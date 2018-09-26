DROP SCHEMA public CASCADE;
CREATE SCHEMA public
  AUTHORIZATION postgres;

GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;
COMMENT ON SCHEMA public
  IS 'standard public schema';

CREATE TABLE exp_levels(
	id SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(50) NOT NULL,
	start_value INTEGER NOT NULL,
	end_value INTEGER NOT NULL
);

CREATE TABLE classes(
	id SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(50) NOT NULL
);

CREATE TABLE teams(
	id SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(50) NOT NULL,
	class_id INTEGER REFERENCES classes(id)
);

CREATE TABLE quests(
	id SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(100) NOT NULL,
	description TEXT NOT NULL,
	value INTEGER DEFAULT 1,
	is_extra BOOLEAN DEFAULT FALSE
);

CREATE TABLE artifacts(
	id SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(100) NOT NULL,
	description TEXT NOT NULL,
	price INTEGER DEFAULT 1,
	is_magic BOOLEAN DEFAULT FALSE
);

CREATE TABLE basic_user_data(
    id SERIAL PRIMARY KEY NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE codecoolers(
	id SERIAL PRIMARY KEY NOT NULL,
	basic_data_id INTEGER REFERENCES basic_user_data(id),
	class_id INTEGER REFERENCES classes(id),
	exp INTEGER DEFAULT 0,
	balance INTEGER DEFAULT 0,
	team_id INTEGER REFERENCES teams(id)
);

CREATE TABLE mentors(
	id SERIAL PRIMARY KEY NOT NULL,
	basic_data_id INTEGER REFERENCES basic_user_data(id),
	class_id INTEGER REFERENCES classes(id)
);

CREATE TABLE admins(
    id SERIAL PRIMARY KEY NOT NULL,
    basic_data_id INTEGER REFERENCES basic_user_data(id)
);

CREATE TABLE done_quests(
	id SERIAL PRIMARY KEY NOT NULL,
	quest_id INTEGER REFERENCES quests(id),
	codecooler_id INTEGER REFERENCES codecoolers(id)
);

CREATE TABLE bought_artifacts(
	id SERIAL PRIMARY KEY NOT NULL,
	artifact_id INTEGER REFERENCES artifacts(id),
	codecooler_id INTEGER REFERENCES codecoolers(id),
	is_used BOOLEAN DEFAULT FALSE
);

CREATE TABLE sessions(
    session_id VARCHAR(50) UNIQUE PRIMARY KEY NOT NULL,
    basic_data_id INTEGER REFERENCES basic_user_data(id),
    account_type INTEGER NOT NULL
);

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO creepyguy;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO creepyguy;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO creepyguy;
