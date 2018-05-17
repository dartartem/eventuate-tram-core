#! /bin/bash

set -e

. ./set-env-postgres-polling.sh

docker-compose -f docker-compose-postgres-polling.yml down -v

docker-compose -f docker-compose-postgres-polling.yml up --build -d

./wait-for-postgres.sh

./gradlew build -x eventuate-tram-status-service:test

docker-compose -f docker-compose-postgres-polling.yml down -v
