#!/usr/bin/env bash
HTTP_PORT=${HTTP_PORT:-8080}
DOMAIN=${DOMAIN:-"localhost"}
HOST="${DOMAIN}:${HTTP_PORT}"

read -d '' CODE << EOF
def ola(params)
  return "🌍 Name= " + params.getString("name")
end
# params is a io.vertx.core.json.JsonObject
EOF
echo "$CODE"

http POST http://${HOST}/functions name="ola" \
  language="ruby" \
  version="0.0.0" \
  code="$CODE"
