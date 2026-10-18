# ==============================================================================
# Production Docker Container for Tomcat 10.1 + Java 17 WAR Application
# Compatible with Render, Railway, Fly.io, and Cloud Container Runtimes
# ==============================================================================

# Stage 1: Build the Maven WAR artifact
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build production WAR
RUN mvn clean package -DskipTests -B

# Stage 2: Production Runtime with Apache Tomcat 10.1 on Eclipse Temurin JDK 17
FROM tomcat:10.1-jdk17-temurin-jammy
WORKDIR /usr/local/tomcat

# Remove default boilerplate Tomcat webapps
RUN rm -rf webapps/*

# Deploy the application as ROOT.war (serving at /)
COPY --from=builder /app/target/HibernateEmployeeManagement.war webapps/ROOT.war

# Expose default port
EXPOSE 8080

# Dynamically bind to $PORT (for Render/Railway/Vercel) or fallback to 8080
CMD ["sh", "-c", "sed -i \"s/port=\\\"8080\\\"/port=\\\"${PORT:-8080}\\\"/g\" /usr/local/tomcat/conf/server.xml && exec catalina.sh run"]
