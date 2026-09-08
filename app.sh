#!/bin/zsh
echo "Building racing service"
cd racing-service
mvn clean install -DskipTests

docker build -t racing-service .
cd ..

echo "Building sidewalk donkey service"
cd sidewalk-donkey
mvn clean install -DskipTests

docker build -t sidewalk-donkey .
cd ..

docker compose up -d