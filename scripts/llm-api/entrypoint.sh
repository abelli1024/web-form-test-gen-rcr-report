#!/bin/sh
set -e

echo "Starting llm-api container..."

until mysqladmin ping -h mysql-db -uroot -p123456 --silent; do
  echo "⏳ waiting for mysql-db..."
  sleep 2
done

# Make sure the database exists
echo "Creating database must_db if not exists..."
mysql -h mysql-db -uroot -p123456 -e "CREATE DATABASE IF NOT EXISTS must_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;"

# Initialize SQL (expressly specify the database)
if [ -f /app/init/must_db.sql ]; then
  echo "Importing must_db.sql into must_db..."
  mysql -h mysql-db -uroot -p123456 must_db < /app/init/must_db.sql
fi

# Start Spring Boot app
echo "Database ready, starting Spring Boot..."
exec java -jar app.jar \
  --server.port=8081 \
  --spring.config.additional-location=/app/config/llm-config.yml
