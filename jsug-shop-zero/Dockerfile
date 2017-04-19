# Reference: https://blog.tiqwab.com/2017/03/21/docker-java.html

FROM openjdk:jdk-alpine
VOLUME /tmp
RUN mkdir /app
WORKDIR /app
RUN adduser -S sboot
USER sboot
ENV JAVA_OPTS=""
ENV JAR_NAME=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar build/libs/$JAR_NAME" ]
