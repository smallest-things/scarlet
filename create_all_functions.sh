#!/usr/bin/env bash
HTTP_PORT=${HTTP_PORT:-8080}
DOMAIN=${DOMAIN:-"localhost"}
HOST="${DOMAIN}:${HTTP_PORT}"

read -d '' CODE << EOF
function hello(params) {
  return {
    message: "Hello World",
    total: 42,
    author: "@k33g_org",
    params: params.getString("name")
  }
}
EOF
echo "$CODE"

http POST http://${HOST}/functions name="hello" \
  language="js" \
  version="0.0.0" \
  code="$CODE"

read -d '' CODE << EOF
def plop(params):
    return "Name is " + params.getString("name")

# params is a io.vertx.core.json.JsonObject


EOF
echo "$CODE"

http POST http://${HOST}/functions name="plop" \
  language="python" \
  version="0.0.0" \
  code="$CODE"

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

read -d '' CODE << EOF
def hey(params)
  return "🌍 Name= " + params.getString("name")
end
# params is a io.vertx.core.json.JsonObject

EOF
echo "$CODE"

http POST http://${HOST}/functions name="hey" \
  language="ruby" \
  version="0.0.0" \
  code="$CODE"
