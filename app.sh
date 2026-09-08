#!/bin/zsh
cd racing-service
mvn clean install -DskipTests

docker build -t racing-service .
cd ..

docker compose up -d