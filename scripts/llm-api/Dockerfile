FROM openjdk:8-jre-slim
WORKDIR /app

COPY must-llm-api-1.0-SNAPSHOT.jar app.jar
COPY llm-config.yml /app/config/llm-config.yml
COPY must_db.sql /app/init/must_db.sql
COPY entrypoint.sh /app/entrypoint.sh

RUN apt-get update \
    && apt-get install -y --no-install-recommends mariadb-client \
    && rm -rf /var/lib/apt/lists/* \
    && chmod +x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"]
