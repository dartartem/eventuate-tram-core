#!/bin/bash

set -e

if [ -z "$DOCKER_COMPOSE" ]; then
    echo setting DOCKER_COMPOSE
    export DOCKER_COMPOSE="docker-compose -f docker-compose-mariadb-binlog.yml -f docker-compose-cdc-mariadb-binlog.yml"
else
    echo using existing DOCKER_COMPOSE = $DOCKER_COMPOSE
fi

export GRADLE_OPTIONS="-P excludeCdcLibs=true"

./gradlew $GRADLE_OPTIONS $* :eventuate-tram-cdc-mysql-service:clean :eventuate-tram-cdc-mysql-service:assemble

. ./set-env-mysql-binlog.sh

$DOCKER_COMPOSE down -v

$DOCKER_COMPOSE build
$DOCKER_COMPOSE up -d mariadb

./wait-for-mysql.sh

$DOCKER_COMPOSE up -d

./wait-for-services.sh $DOCKER_HOST_IP 8099

./gradlew $GRADLE_OPTIONS :eventuate-tram-mysql-kafka-integration-test:cleanTest :eventuate-tram-mysql-kafka-integration-test:test

$DOCKER_COMPOSE stop
$DOCKER_COMPOSE rm --force -v
