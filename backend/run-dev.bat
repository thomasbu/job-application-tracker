@echo off
echo Starting Job Tracker Backend (Development Profile)...
set SPRING_PROFILES_ACTIVE=dev
mvnw.cmd spring-boot:run