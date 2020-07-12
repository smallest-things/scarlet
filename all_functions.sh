#!/usr/bin/env bash

# run it like that:
# export DOMAIN="myhome.test"; export HTTP_PORT=9090; ./all_functions.sh
# or use default values

HTTP_PORT=${HTTP_PORT:-8080}
DOMAIN=${DOMAIN:-"localhost"}
HOST="${DOMAIN}:${HTTP_PORT}"

http GET http://${HOST}/functions
