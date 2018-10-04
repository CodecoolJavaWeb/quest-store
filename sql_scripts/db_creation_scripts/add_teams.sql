INSERT INTO teams (team_name, class_id) VALUES 
(
	('team_one'),
	(SELECT id FROM classes WHERE class_name = 'Programming Basics')
),
(
	('team_two'),
	(SELECT id FROM classes WHERE class_name = 'OOP with Java')
);
