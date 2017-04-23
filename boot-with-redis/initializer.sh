#!/bin/sh

projectName=boot-with-redis
dependencies=web,lombok,thymeleaf,jpa,h2,redis

curl https://start.spring.io/starter.tgz -d dependencies=$dependencies -d type=gradle-project -d groupId=com.tiqwab artifactId=$projectName packageName=com.tiqwab.example name=$projectName | tar -xzvf -
