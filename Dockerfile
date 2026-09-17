FROM ubuntu:22.04 AS builder

ENV DEBIAN_FRONTEND=noninteractive
# Install JDK 17, wget, and unzip
RUN apt-get update && apt-get install -y openjdk-17-jdk wget unzip

# Set up Android SDK (Required because the KMP :core module has an Android target)
ENV ANDROID_HOME=/opt/android-sdk
RUN mkdir -p ${ANDROID_HOME}/cmdline-tools
RUN wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O /tmp/cmdline-tools.zip
RUN unzip -q /tmp/cmdline-tools.zip -d ${ANDROID_HOME}/cmdline-tools/
RUN mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest

# Accept Android licenses and install platform tools
RUN yes | ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager "platforms;android-34" "build-tools;34.0.0"
RUN yes | ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager --licenses

WORKDIR /app
# Copy the entire project
COPY . .

# Build the Ktor server distribution
RUN ./gradlew :server:installDist --no-daemon

# Create a lightweight runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built Ktor server from the builder stage
COPY --from=builder /app/server/build/install/server/ /app/

# Render exposes PORT env var
EXPOSE $PORT

# Start the server
CMD ["./bin/server"]
