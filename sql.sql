CREATE DATABASE hehe;
USE hehe;

CREATE USER 'hot_feed_user'@'localhost' IDENTIFIED BY 'test';

GRANT ALL ON hehe.* TO 'hot_feed_user'@'localhost';

CREATE TABLE IF NOT EXISTS hot_album(id INT PRIMARY KEY,type INT,`title` VARCHAR(255) NOT NULL UNIQUE,content TEXT,`from` INT,state INT DEFAULT -1,insert_time TIMESTAMP DEFAULT  CURRENT_TIMESTAMP(),show_time INT DEFAULT 0)