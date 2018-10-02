SELECT s.id, b.first_name, b.last_name, b.email, b.password, c.class_name, s.exp, s.balance, t.team_name FROM 
(((codecoolers AS s INNER JOIN basic_user_data AS b ON s.basic_data_id = b.id)
INNER JOIN classes AS c ON s.class_id = c.id)
LEFT JOIN teams AS t ON s.team_id = t.id);