FROM openjdk:8-jdk-alpine
MAINTAINER jysoft Service@51e.com.cn
WORKDIR /app
ADD jybasic-api.jar /app/jybasic-api.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app/jybasic-api.jar"]