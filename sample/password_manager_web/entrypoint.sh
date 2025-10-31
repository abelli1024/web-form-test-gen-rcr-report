#!/bin/sh
set -e

# 等待 MySQL 启动
until mysqladmin ping -hmysql-db -uroot -p123456 --silent; do
  echo "waiting for mysql-db..."
  sleep 2
done

# 创建数据库（如果不存在）
mysql -hmysql-db -uroot -p123456 -e "CREATE DATABASE IF NOT EXISTS must_test_password_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"

# 启动 Spring Boot 应用并加载外部配置
exec java -jar /app/app.jar --spring.config.location=file:/config/application-docker.yml
