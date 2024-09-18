#Stage 1
# initialize build and set base image for first stage
FROM maven:3.9.4-eclipse-temurin-21 AS stage1

# speed up Maven JVM a bit
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

# set working directory
WORKDIR /usr/src/app

# copy just pom.xml
COPY pom.xml .

# go-offline using the pom.xml
RUN mvn dependency:go-offline

# copy your other files
COPY ./src ./src

# compile the source code and package it in a jar file
RUN mvn clean install -Dmaven.test.skip=true -X


#Stage 2
#jdk has all the files and compiler, libraries, dependencies of the app. So its bigger than the JRE
#JRE only has the runtime, so its lighter/smaller than JDK

# Set the base image to a minimal OpenJDK 21 JRE (using Alpine for a small image)
FROM eclipse-temurin:21-jre-alpine

# set deployment directory
WORKDIR /usr/src/app

# copy over the built artifact from the maven image
COPY --from=stage1 /usr/src/app/target/mobilebankapp-0.0.1-SNAPSHOT.jar limabankapp.jar

# Specify the entry point
ENTRYPOINT ["java", "-jar", "limabankapp.jar"]