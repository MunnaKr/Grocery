# Use JDK 21 for building (matching the project's likely requirement for newer Gradle/AGP)
FROM openjdk:21-jdk-slim AS build
WORKDIR /app

# Copy the entire project
COPY . .

# Grant execute permission to gradlew
RUN chmod +x gradlew

# Build the server distribution
# We use --no-daemon to avoid issues in container builds
RUN ./gradlew :server:installDist --no-daemon

# Run stage
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy the build artifacts from the build stage
COPY --from=build /app/server/build/install/server /app/server

# Expose the port (Render will override this via environment variable)
EXPOSE 8080

# Start the server
# The bin/server script is created by the application plugin's installDist task
CMD ["/app/server/bin/server"]
