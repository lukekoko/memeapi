#!/bin/bash

# create .env file
rm -f -- .env
touch .env
# add env variable to .env file
echo "REDDIT_CLIENTID=$REDDIT_CLIENTID" >> .env
echo "REDDIT_CLIENTSECRET=$REDDIT_CLIENTSECRET" >> .env

# docker-compose up
docker-compose pull
docker-compose up --force-recreate --build -d
docker image prune -f