# Scarlet: a small dynamic MQTT Server

Scarlet is a **MQTT Server** developed with Vert.x, GraalVM and Kotlin.

> 🖐️ This project is a **mirror from 🦊 GitLab** to GitHub, if you're reading this from [https://github.com/smallest-things/scarlet](https://github.com/smallest-things/scarlet), you must know that the **single source of truth** is on [https://gitlab.com/smallest-things/scarlet](https://gitlab.com/smallest-things/scarlet)

> 👀 You can follow what is planned here [https://gitlab.com/smallest-things/scarlet/-/issues/1](https://gitlab.com/smallest-things/scarlet/-/issues/1) and the current activity of the project here [https://gitlab.com/smallest-things/scarlet/-/boards](https://gitlab.com/smallest-things/scarlet/-/boards)


## What is "magic"?

You can add **"functions"** (JavaScript, Python, Ruby) to Scarlet with a REST API, and then you can call these functions from http request or with a **MQTT client**.

> 🚧 WIP there is no persistence of the functions right now (stay tuned)

## Build Scarlet

### Requirements

- Install GraalVM
- Install embedded languages:
    ```bash
    gu install python
    gu install ruby
    ```
### Build

```bash
mvn clean package
```

## How to use Scarlet

```bash
java -jar target/scarlet-0.0.0-SNAPSHOT-fat.jar
```

> Remarks:
> - default http port is `8080` (use `HTTP_PORT` to change the value)
> - default mqtt port is `1883` (use `MQTT_PORT` to change the value)

### Create functions

You need to create the source code of a **named** function, and then do a post http request to **Scarlet** with this json payload:

```json
{
  "name": "function_name",
  "language": "used_language",
  "version": "0.0.0",
  "code": "source_code_of_the_function"
}
```

> I use [HTTPie](https://httpie.org/) to do my http requests (but you can use curl of course 😉)

#### Create a JavaScript function

```bash
HOST="localhost:8080"
read -d '' CODE << EOF
function hello(params) {
  return {
    message: "Hello World",
    params: params.getString("name")
  }
}
EOF

http POST http://${HOST}/functions name="hello" \
  language="js" \
  version="0.0.0" \
  code="$CODE"
```

> test the function with: `http POST http://${HOST}/execute/hello name="Bob Morane"`


#### Create a Python function

```bash
HOST="localhost:8080"
read -d '' CODE << EOF
def plop(params):
    return "Name is " + params.getString("name")
# params is a io.vertx.core.json.JsonObject
EOF

http POST http://${HOST}/functions name="plop" \
  language="python" \
  version="0.0.0" \
  code="$CODE"
```

> test the function with: `http POST http://${HOST}/execute/plop name="Bob Morane"`


#### Create a Ruby function

```bash
HOST="localhost:8080"
read -d '' CODE << EOF
def ola(params)
  return "🌍 Name= " + params.getString("name")
end
# params is a io.vertx.core.json.JsonObject
EOF

http POST http://${HOST}/functions name="ola" \
  language="ruby" \
  version="0.0.0" \
  code="$CODE"
```

> test the function with: `http POST http://${HOST}/execute/ola name="Bob Morane"`

### 🖐 Use a MQTT client

The examples use `MQTT.js`

You can send three kinds of MQTT messages:

#### json message with call of a function

You have to set the **function name** and the **parameters** like that:

```javascript
// simple text message
client.publish('topic_name', JSON.stringify({function:"hello", params:{name:"bob morane"}}))
```

All the subscribed clients will receive the following Json payload with the result of the function:

```javascript
{ result: '{message: "Hello World", params: "bob"}' }
// where result is a string
```

#### simple text message

```javascript
// simple text message
client.publish('topic_name', "👋 hello world 🌍")
```

All the subscribed clients will receive the following Json payload:

```javascript
{message: "👋 hello world 🌍"}
```

#### json message

```javascript
// json message
client.publish('topic_name', JSON.stringify({message:"hello world"}))
```

All the subscribed clients will receive the following Json payload:

```javascript
{result: '{"message":"hello world"}'}
// where result is a string
```

## Test Scarlet with MQTT.js

- run **Scarlet**: `java -jar target/scarlet-0.0.0-SNAPSHOT-fat.jar`
- create a set of functions: `./create_all_functions.sh`
- See `./testing/mqtt_js`:
  - run `node wait.js`
  - run `node send.js`

## Functions persistence

At the first launch, **Scarlet** will create a `./storage` directory with 2 subdirectories:
- `./storage/functions`
- `./storage/clients` (not used right now)

All created functions are stored in `./storage/functions` with a filename constructed like that:
```
<function_name>@<version>.<extension>
```

At every start of **Scarlet**, all functions of `./storage` are loaded and compiled.

> - if you have several version of a function, the last one overrides the others
> - you can override the path `./storage` with this environment variable: `STORAGE_PATH`

## References

- [Compile and Run a Polyglot Application](https://www.graalvm.org/docs/reference-manual/embed/)
