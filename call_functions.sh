#!/usr/bin/env bash
HOST="localhost:8080"

http POST http://${HOST}/execute/hello name="Bob Morane"
http POST http://${HOST}/execute/ola name="Jane Doe"
http POST http://${HOST}/execute/plop name="John Doe"
