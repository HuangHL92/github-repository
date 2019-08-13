FROM openjdk:8-jdk-alpine
MAINTAINER jysoft Service@51e.com.cn
WORKDIR /app
ADD dataCenter-api.jar /app/dataCenter-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app/dataCenter-api.jar"]