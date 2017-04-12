#!/bin/sh

curl https://start.spring.io/starter.tgz -d dependencies=web,security,lombok,thymeleaf,jpa,h2 -d type=gradle-project -d groupId=com.tiqwab artifactId=jsug-shop-zero packageName=com.tiqwab.example name=jsug-shop-zero | tar -xzvf -
