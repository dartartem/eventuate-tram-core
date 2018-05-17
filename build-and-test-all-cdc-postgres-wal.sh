#!/bin/bash

set -e

if [ -z "$DOCKER_COMPOSE" ]; then
    echo setting DOCKER_COMPOSE
    export DOCKER_COMPOSE="docker-compose -f docker-compose-postgres-wal.yml -f docker-compose-cdc-postgres-wal.yml"
else
    echo using existing DOCKER_COMPOSE = $DOCKER_COMPOSE
fi

export GRADLE_OPTIONS="-P excludeCdcLibs=true"

./gradlew $GRADLE_OPTIONS $* :eventuate-tram-cdc-mysql-service:clean :eventuate-tram-cdc-mysql-service:assemble

. ./set-env-postgres-wal.sh

$DOCKER_COMPOSE stop
$DOCKER_COMPOSE rm --force -v

$DOCKER_COMPOSE build
$DOCKER_COMPOSE up -d postgres

./wait-for-postgres.sh

$DOCKER_COMPOSE up -d

./wait-for-services.sh $DOCKER_HOST_IP 8099

./gradlew $GRADLE_OPTIONS :eventuate-tram-status-service:cleanTest :eventuate-tram-status-service:test
./gradlew $GRADLE_OPTIONS :eventuate-tram-mysql-kafka-integration-test:cleanTest :eventuate-tram-mysql-kafka-integration-test:test

$DOCKER_COMPOSE stop
$DOCKER_COMPOSE rm --force -v



