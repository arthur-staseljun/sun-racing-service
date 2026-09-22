#!/bin/zsh
mvn clean install -DskipTests
docker build -t racing-service .
docker compose up -d