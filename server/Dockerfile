# Build the API with Java 21
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace/server
# Use this directory (containing gradlew and settings.gradle) as the build context.
COPY . ./
# Make the wrapper executable on Linux and package the API with its dependencies
RUN chmod +x gradlew && ./gradlew --no-daemon :server:shadowJar

# Use the smaller Java runtime image for the running application
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Run as a dedicated non-root user with a writable application directory
RUN groupadd --system splendor && useradd --system --gid splendor splendor \
    && chown splendor:splendor /app
# Copy only the runnable JAR from the build stage, leaving source and build tools behind
COPY --from=build --chown=splendor:splendor /workspace/server/server/build/libs/*-fat.jar /app/server.jar
USER splendor
# Default listening port; hosting platforms can override PORT at runtime
ENV PORT=8080
# Limit heap to 60% of container memory and exit if the JVM runs out of memory
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=60.0 -XX:+ExitOnOutOfMemoryError"
# Document the default port; publishing it requires a host port mapping
EXPOSE 8080
# Start the API. The frontend is deployed separately on Vercel
CMD ["java", "-jar", "/app/server.jar"]
