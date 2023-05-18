ALTER TABLE courses ADD status tinyint;

UPDATE courses SET status = 1;