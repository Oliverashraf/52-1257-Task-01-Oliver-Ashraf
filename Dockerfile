# 1. Base Image
FROM eclipse-temurin:25.0.2_10-jdk

# 2. Working directory
WORKDIR /app

# 3. Copy the JAR (ensure the name matches your target folder result)
COPY target/*.jar /app/app.jar
# 4. Copy JSON data files into the directory specified in the task
RUN mkdir -p /data
COPY src/main/resources/users.json /data/users.json
COPY src/main/resources/notes.json /data/notes.json

# 5. Environment variables (Docker defaults)
ENV USER_NAME=Docker_Oliver_Ashraf
ENV ID=Docker_52_1257
# Force the port to 8080 for the Compose mapping requirement
ENV SERVER_PORT=8080

# 6. Expose the port
EXPOSE 8080

# 7. Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]