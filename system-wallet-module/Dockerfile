# Этап 1: Сборка через Gradle
FROM gradle:8-jdk21 AS build
WORKDIR /app

# Копируем абсолютно ВСЕ файлы из корня проекта, чтобы Gradle видел структуру модулей
COPY . .

# Собираем строго наш микросервис
RUN ./gradlew :system-wallet-module:bootJar -x test

# Этап 2: Запуск готового приложения
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Копируем собранный jar-файл
COPY --from=build /app/system-wallet-module/build/libs/*.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]
