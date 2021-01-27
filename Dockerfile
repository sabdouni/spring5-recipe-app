FROM centos:8

RUN yum update -y && \
    yum install java -y

COPY target/spring5-recipe-app-*.jar /spring5-recipe-app/spring5-recipe-app.jar

VOLUME /tmp
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/spring5-recipe-app/spring5-recipe-app.jar"]