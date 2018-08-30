CREATE TABLE exp_level(
	id SERIAL PRIMARY KEY NOT NULL,
	name TEXT NOT NULL,
	start_value INTEGER NOT NULL,
	end_value INTEGER NOT NULL
);

CREATE TABLE classes(
	id SERIAL PRIMARY KEY NOT NULL,
	name TEXT NOT NULL
);

CREATE TABLE teams(
	id SERIAL PRIMARY KEY NOT NULL,
	name TEXT NOT NULL,
	class_id INTEGER REFERENCES classes(id)
);

CREATE TABLE quests(
	id SERIAL PRIMARY KEY NOT NULL,
	name TEXT NOT NULL,
	description TEXT NOT NULL,
	value INTEGER DEFAULT 1,
	is_extra BOOLEAN DEFAULT FALSE
);

CREATE TABLE artifacts(
	id SERIAL PRIMARY KEY NOT NULL,
	name TEXT NOT NULL,
	description TEXT NOT NULL,
	price INTEGER DEFAULT 1,
	is_magic BOOLEAN DEFAULT FALSE
);

CREATE TABLE codecoolers(
	id SERIAL PRIMARY KEY NOT NULL,
	first_name TEXT NOT NULL,
	last_name TEXT NOT NULL,
	email TEXT NOT NULL,
	password TEXT NOT NULL,
	class_id INTEGER REFERENCES classes(id),
	exp INTEGER DEFAULT 0,
	balance INTEGER DEFAULT 0,
	team_id INTEGER REFERENCES teams(id)
);

CREATE TABLE mentors(
	id SERIAL PRIMARY KEY NOT NULL,
	first_name TEXT NOT NULL,
	last_name TEXT NOT NULL,
	email TEXT NOT NULL,
	password TEXT NOT NULL,
	class_id INTEGER REFERENCES classes(id)
);

CREATE TABLE done_quests(
	id SERIAL PRIMARY KEY NOT NULL,
	quest_id INTEGER REFERENCES quests(id),
	codecooler_id INTEGER REFERENCES codecoolers(id)
);

CREATE TABLE used_artifacts(
	id SERIAL PRIMARY KEY NOT NULL,
	artifact_id INTEGER REFERENCES artifacts(id),
	codecooler_id INTEGER REFERENCES codecoolers(id)
);

CREATE TABLE currently_funding(
	id SERIAL PRIMARY KEY NOT NULL,
	artifact_id INTEGER REFERENCES artifacts(id),
	team_id INTEGER REFERENCES teams(id)
);

CREATE TABLE donations(
	id SERIAL PRIMARY KEY NOT NULL,
	codecooler_id INTEGER REFERENCES codecoolers(id),
	curr_fund_id INTEGER REFERENCES currently_funding(id),
	amount INTEGER NOT NULL
);
