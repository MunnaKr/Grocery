# Stage 1: Build stage with JDK 17 and Android SDK (required for AGP project configuration)
FROM ubuntu:22.04 AS build

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y openjdk-17-jdk wget unzip

# Set up Android SDK
ENV ANDROID_HOME=/opt/android-sdk
RUN mkdir -p ${ANDROID_HOME}/cmdline-tools \
    && wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O /tmp/cmdline-tools.zip \
    && unzip -q /tmp/cmdline-tools.zip -d ${ANDROID_HOME}/cmdline-tools/ \
    && mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest \
    && rm /tmp/cmdline-tools.zip

RUN yes | ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager "platforms;android-34" "build-tools;34.0.0" \
    && yes | ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager --licenses

WORKDIR /app

# Copy project files
COPY . .

# Grant execute permission to gradlew
RUN chmod +x gradlew

# Build the Ktor server fat JAR
RUN ./gradlew :server:buildFatJar --no-daemon

# Stage 2: Lightweight runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built fat jar from the build stage
COPY --from=build /app/server/build/libs/server-all.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
