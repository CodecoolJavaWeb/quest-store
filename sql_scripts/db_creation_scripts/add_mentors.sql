INSERT INTO basic_user_data 
(first_name, last_name, email, password) 
VALUES 
('Mariusz', 'Chudy', 'mariusz.chudy@codecool.com', 'chudy'),
('Marian', 'Czech', 'marian.czech@codecool.com', 'czech'),
('Adam', 'Stopka', 'adam.stopka@codecool.com', 'stopka'),
('Anna', 'Morawic', 'anna.morawiec@codecool.com', 'morawiec'),
('Grzegorz', 'Barcikowski', 'grzegorz.barcikowski@codecool.com', 'barcikowski'),
('Jerzy', 'Ptaszyński', 'jerzy.ptaszynski@codecool.com', 'ptaszynski'),
('Damian', 'Matkowski', 'damian.matkowski@codecool.com', 'matkowski'),
('Marcin', 'Kubacki', 'marcin.kubacki@codecool.com', 'kubacki');

INSERT INTO mentors (basic_data_id, class_id) VALUES 
(
	(SELECT id FROM basic_user_data WHERE email = 'mariusz.chudy@codecool.com'),
	(SELECT id FROM classes WHERE class_name = 'Programming Basics')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'marian.czech@codecool.com'),
	(SELECT id FROM classes WHERE class_name = 'Programming Basics')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'adam.stopka@codecool.com'),
	(SELECT id FROM classes WHERE class_name = 'OOP with Java')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'anna.morawiec@codecool.com'),
	(SELECT id FROM classes WHERE class_name = 'OOP with Java')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'grzegorz.barcikowski@codecool.com'),
	(SELECT id FROM classes WHERE class_name = 'Web and SQL with Java')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'jerzy.ptaszynski@codecool.com'),
	(SELECT id FROM classes WHERE class_name = 'Web and SQL with Java')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'damian.matkowski@codecool.com'),
	(SELECT id FROM classes WHERE class_name = 'Advanced')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'marcin.kubacki@codecool.com'),
	(SELECT id FROM classes WHERE class_name = 'Advanced')
);