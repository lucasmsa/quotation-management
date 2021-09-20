
# Build stage
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /Users/lucasmoreira/Workspace/Inatel/quotation-management
COPY pom.xml /Users/lucasmoreira/Workspace/Inatel/quotation-management
RUN mvn -f /Users/lucasmoreira/Workspace/Inatel/quotation-management/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /Users/lucasmoreira/Workspace/Inatel/quotation-management/target/demo-0.0.1-SNAPSHOT.jar /Users/lucasmoreira/local/lib/demo.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/Users/lucasmoreira/local/lib/demo.jar"]