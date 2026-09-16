FROM gradle:8.7-jdk17 AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew :server:buildFatJar --no-daemon
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY server/build/libs/server-all.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
