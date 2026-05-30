create database if not exists life_is_bananas_security
    character set utf8mb4
    collate utf8mb4_unicode_ci;

create user if not exists 'lifeisbananas_user'@'localhost' identified by 'lifeisbananas_pw';
grant all privileges on life_is_bananas_security.* to 'lifeisbananas_user'@'localhost';
flush privileges;
