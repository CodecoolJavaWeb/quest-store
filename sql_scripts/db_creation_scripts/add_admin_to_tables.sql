INSERT INTO basic_user_data 
(first_name, last_name, email, password) 
VALUES 
('Creepy', 'Guy', 'admin@codecool.com', 'magic');

INSERT INTO admins (basic_data_id) VALUES (
	(SELECT id FROM basic_user_data WHERE email = 'admin@codecool.com')
);