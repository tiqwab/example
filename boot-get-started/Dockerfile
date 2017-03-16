FROM openjdk:jdk-alpine
VOLUME /tmp
RUN mkdir /app
WORKDIR /app
# ADD build/libs/boot-get-started-0.0.1-SNAPSHOT.jar app.jar
# RUN sh -c 'touch /app.jar'
# RUN useradd -m sboot
RUN adduser -S sboot
USER sboot
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar build/libs/boot-get-started-0.0.1-SNAPSHOT.jar" ]
