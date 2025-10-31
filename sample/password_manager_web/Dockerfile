FROM openjdk:17-jdk-slim

WORKDIR /app

COPY password-manager-0.0.1-SNAPSHOT.jar /app/app.jar
COPY entrypoint.sh /app/entrypoint.sh

RUN apt-get update \
    && apt-get install -y --no-install-recommends mariadb-client \
    && rm -rf /var/lib/apt/lists/* \
    && chmod +x /app/entrypoint.sh

# 暴露应用端口
EXPOSE 8080

# 入口命令
ENTRYPOINT ["/app/entrypoint.sh"]
