CREATE TABLE user (
    id int unsigned PRIMARY KEY AUTO_INCREMENT,
    first_name nvarchar(20) NOT NULL DEFAULT '',
    last_name nvarchar(20) NOT NULL DEFAULT ''
);
