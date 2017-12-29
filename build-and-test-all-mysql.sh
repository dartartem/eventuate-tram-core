#! /bin/bash

set -e

. ./set-env-mysql.sh

docker-compose -f docker-compose-mysql.yml down -v

docker-compose -f docker-compose-mysql.yml up --build -d

./wait-for-mysql.sh

./gradlew build

docker-compose -f docker-compose-mysql.yml down -v
