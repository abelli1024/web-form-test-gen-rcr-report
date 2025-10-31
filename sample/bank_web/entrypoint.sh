#!/bin/sh
set -e

until mysqladmin ping -hmysql-db -uroot -p123456 --silent; do
  echo "waiting for mysql-db..."
  sleep 2
done

# Create a database (if not exists)
mysql -hmysql-db -uroot -p123456 -e "CREATE DATABASE IF NOT EXISTS must_test_bank CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"

# Start Spring Boot and load external configuration files
exec java -jar /app/app.war --spring.config.location=file:/config/application-docker.properties
