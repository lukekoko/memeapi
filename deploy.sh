#!/bin/bash

# create .env file
touch .env
# add env variable to .env file
echo $'REDDIT_CLIENTID=${REDDIT_CLIENTID}\n' >> .env
echo $'REDDIT_CLIENTSECRET=${REDDIT_CLIENTSECRET}\n' >> .env

# docker-compose up
