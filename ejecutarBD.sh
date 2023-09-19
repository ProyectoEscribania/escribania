#!/bin/bash


cd ~/IdeaProjects/CanchaApp

mvn clean install

mvn -pl webapp spring-boot:run -Pjdbc-postgresql

