INSERT INTO basic_user_data 
(first_name, last_name, email, password) 
VALUES 
('Jan', 'Kowalski', 'kowalski@gmail.com', 'kowalski'),
('Bogdan', 'Oleszek', 'oleszek@gmail.com', 'oleszek'),
('Adam', 'Szewczyk', 'szewczyk@gmail.com', 'szewczyk'),
('Tadeusz', 'Kaniowski', 'kaniowski@gmail.com', 'kaniowski'),
('Teresa', 'Walczak', 'walczak@gmail.com', 'walczak'),
('Monika', 'Kuźniar', 'kuzniar@gmail.com', 'kuzniar'),
('Lucjan', 'Czechowicz', 'czechowicz@gmail.com', 'czechowicz'),
('Marcin', 'Panas', 'panas@gmail.com', 'panas');

INSERT INTO codecoolers (basic_data_id, class_id) VALUES
(
	(SELECT id FROM basic_user_data WHERE email = 'kowalski@gmail.com'),
	(SELECT id FROM classes WHERE class_name = 'Programming Basics')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'oleszek@gmail.com'),
	(SELECT id FROM classes WHERE class_name = 'Programming Basics')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'szewczyk@gmail.com'),
	(SELECT id FROM classes WHERE class_name = 'OOP with Java')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'kaniowski@gmail.com'),
	(SELECT id FROM classes WHERE class_name = 'OOP with Java')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'walczak@gmail.com'),
	(SELECT id FROM classes WHERE class_name = 'Web and SQL with Java')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'kuzniar@gmail.com'),
	(SELECT id FROM classes WHERE class_name = 'Web and SQL with Java')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'czechowicz@gmail.com'),
	(SELECT id FROM classes WHERE class_name = 'Advanced')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'panas@gmail.com'),
	(SELECT id FROM classes WHERE class_name = 'Advanced')
);