INSERT INTO basic_user_data 
(first_name, last_name, email, password) 
VALUES 
('Mariusz', 'Chudy', 'm.chudy@codecool.com', 'chudy'),
('Marian', 'Czech', 'm.czech@codecool.com', 'czech');

INSERT INTO mentors (basic_data_id, class_id) VALUES 
(
	(SELECT id FROM basic_user_data WHERE email = 'm.chudy@codecool.com'),
	(SELECT id FROM classes WHERE name = 'Java')
),
(
	(SELECT id FROM basic_user_data WHERE email = 'm.czech@codecool.com'),
	(SELECT id FROM classes WHERE name = 'Java')
);