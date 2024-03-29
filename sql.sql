CREATE DATABASE hehe;
USE hehe;

CREATE USER 'hot_feed_user'@'localhost' IDENTIFIED BY 'test';

GRANT ALL ON hehe.* TO 'hot_feed_user'@'localhost';

CREATE TABLE IF NOT EXISTS hot_album(id INT PRIMARY KEY,type INT,`title` VARCHAR(255) NOT NULL UNIQUE,content TEXT,`from` INT,state INT DEFAULT -1,insert_time TIMESTAMP DEFAULT  CURRENT_TIMESTAMP(),show_time INT DEFAULT 0)

#dump data
#sed操作过滤特殊字符，解决导入失败的问题
mysqldump --database hehe --user=hot_feed_user --password=test --extended-insert --insert-ignore  --skip-triggers --compact --no-create-info --tables hot_feed --where="id<3" | sed "s/\\\'/''/g" > "D:/TEST/feed.sql"

UPDATE hehe.hot_feed set show_time=show_time+INTERVAL 6 Hour where id>240;

#update show_time
UPDATE hot_feed set show_time=TIMESTAMPADD(MINUTE,TIMESTAMPDIFF(MINUTE,'2014-06-19 08:03:31','2014-06-19 12:03:31'),show_time) where id>240;
UPDATE hot_feed SET show_time = DATE_ADD(show_time, INTERVAL 8 Hour)  where id>240;

select DATE_ADD(show_time, INTERVAL 1 MINUTE) from hehe.hot_feed where id>240;


--tables pic --where="id>115810 and id<=120000" > "D:/TEST/pic.sql"

CREATE INDEX pic_feedid_index ON pic (feed_id)