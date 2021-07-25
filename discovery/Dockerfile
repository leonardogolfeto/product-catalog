FROM openjdk:11-jdk

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/application.jar

EXPOSE 8761
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/usr/share/application.jar"]