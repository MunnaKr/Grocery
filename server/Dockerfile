
# Stage 1: Build the Kotlin server
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew :server:buildFatJar --no-daemon

# Stage 2: Run the server
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/server/build/libs/server-all.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]