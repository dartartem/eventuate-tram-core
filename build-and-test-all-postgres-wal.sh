#! /bin/bash

set -e

. ./set-env-postgres-wal.sh

docker-compose -f docker-compose-postgres-wal.yml down -v

docker-compose -f docker-compose-postgres-wal.yml up --build -d

./wait-for-postgres.sh

./gradlew build -x eventuate-tram-status-service:test

docker-compose -f docker-compose-postgres-wal.yml down -v
