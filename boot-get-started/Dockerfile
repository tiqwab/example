# use alpine as base image
FROM openjdk:jdk-alpine

# recommended by spring boot
VOLUME /tmp

# create directory for application
RUN mkdir /app
WORKDIR /app

# ADD build/libs/boot-get-started-0.0.1-SNAPSHOT.jar app.jar
# RUN sh -c 'touch /app.jar'
# RUN useradd -m sboot

# add user for application
RUN adduser -S sboot
USER sboot

# set entrypoint to execute spring boot application
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar build/libs/boot-get-started-0.0.1-SNAPSHOT.jar" ]
