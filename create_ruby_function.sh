#!/usr/bin/env bash
HOST="localhost:8080"
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
