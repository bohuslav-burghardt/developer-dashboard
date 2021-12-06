### STAGE 1: Build Frontend ###
FROM node:17-alpine AS build-frontend
WORKDIR /usr/src/app
COPY developer-dashboard-ui/package.json developer-dashboard-ui/package-lock.json ./
RUN npm install
COPY developer-dashboard-ui/. .
RUN npm run build

### STAGE 2: Build Java ###
FROM maven:3-jdk-8 AS build-backend
WORKDIR /springbuild
COPY developer-dashboard-backend/pom.xml pom.xml
RUN mvn -q -ntp -B dependency:go-offline
COPY developer-dashboard-backend/. ./
COPY --from=build-frontend /usr/src/app/dist/my-first-project /springbuild/src/main/resources/static
RUN mvn clean package

### STAGE 3: Run ###
FROM openjdk:8-jdk-alpine AS run
COPY --from=build-backend /springbuild/target/developer-dashboard.jar /tmp/app.jar
ENTRYPOINT ["java","-jar","/tmp/app.jar"]