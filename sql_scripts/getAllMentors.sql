SELECT m.id, b.first_name, b.last_name, b.email, b.password, c.class_name FROM
((mentors AS m INNER JOIN basic_user_data AS b ON m.basic_data_id = b.id) 
INNER JOIN classes AS c ON m.class_id = c.id);