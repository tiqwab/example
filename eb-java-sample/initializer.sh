#!/bin/sh

projectName=eb-sample-app
dependencies=web,thymeleaf

curl https://start.spring.io/starter.tgz -d dependencies=$dependencies -d type=gradle-project -d groupId=com.tiqwab artifactId=$projectName packageName=com.tiqwab.example name=$projectName | tar -xzvf -
