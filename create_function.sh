#!/usr/bin/env bash
HTTP_PORT=${HTTP_PORT:-8080}
DOMAIN=${DOMAIN:-"localhost"}
HOST="${DOMAIN}:${HTTP_PORT}"

read -d '' CODE << EOF
function hello(params) {
  return {
    message: "Hello World",
    params: params.getString("name")
  }
}
EOF
echo "$CODE"

http POST http://${HOST}/functions name="hello" \
  language="js" \
  version="0.0.0" \
  code="$CODE"
