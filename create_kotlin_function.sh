#!/bin/bash
HTTP_PORT=${HTTP_PORT:-8080}
DOMAIN=${DOMAIN:-"localhost"}
HOST="${DOMAIN}:${HTTP_PORT}"

read -d '' CODE << EOF
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.ext.web.RoutingContext
import io.vertx.core.json.JsonObject

fun yo(params: JsonObject): Any {
  return json {
    obj(
      "message" to "😍 Hello World!!!",
      "total" to 42,
      "name" to params.getString("name")
    )
  }.encodePrettily()
}
EOF
echo "$CODE"

http POST http://${HOST}/functions name="yo" \
  language="kotlin" \
  version="0.0.0" \
  code="$CODE"
