CREATE DATABASE IF NOT EXISTS user_auth_service;
CREATE DATABASE IF NOT EXISTS product_catalog_service;
CREATE DATABASE IF NOT EXISTS orderservice;
CREATE DATABASE IF NOT EXISTS paymentservice;

CREATE USER IF NOT EXISTS 'skylags_user_2'@'%' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON user_auth_service.* TO 'skylags_user_2'@'%';
GRANT ALL PRIVILEGES ON product_catalog_service.* TO 'skylags_user_2'@'%';
GRANT ALL PRIVILEGES ON orderservice.* TO 'skylags_user_2'@'%';
GRANT ALL PRIVILEGES ON paymentservice.* TO 'skylags_user_2'@'%';
FLUSH PRIVILEGES;
