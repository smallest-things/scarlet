#!/bin/bash
HTTP_PORT=${HTTP_PORT:-8080}
DOMAIN=${DOMAIN:-"localhost"}
HOST="${DOMAIN}:${HTTP_PORT}"

http POST http://${HOST}/execute/hello name="Bob Morane"
http POST http://${HOST}/execute/ola name="Jane Doe"
http POST http://${HOST}/execute/plop name="John Doe"
