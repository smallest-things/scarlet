#!/usr/bin/env bash

# if you define an admin token like this when launching Scarlet
# export SCARLET_ADMIN_TOKEN="tada"; java -jar target/scarlet-0.0.0-SNAPSHOT-fat.jar
# you need to add a header when you query Scarlet

HOST="localhost:8080"
read -d '' CODE << EOF
function hello(params) {
  return {
    message: "Hello World",
    params: params.getString("name")
  }
}
EOF
echo "$CODE"

http POST http://${HOST}/functions name="hello" SCARLET_ADMIN_TOKEN:tada\
  language="js" \
  version="0.0.0" \
  code="$CODE"
