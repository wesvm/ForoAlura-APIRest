ALTER TABLE replies ADD modification_date DATETIME;
ALTER TABLE topics ADD modification_date DATETIME;

UPDATE replies SET modification_date=null;
UPDATE topics SET modification_date=null;