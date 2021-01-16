# Scarlet: a small dynamic MQTT Server

Scarlet is a **MQTT Server** developed with Vert.x and Kotlin.

> 🖐️ This project is a **mirror from 🦊 GitLab** to GitHub, if you're reading this from [https://github.com/smallest-things/scarlet](https://github.com/smallest-things/scarlet), you must know that the **single source of truth** is on [https://gitlab.com/smallest-things/scarlet](https://gitlab.com/smallest-things/scarlet)

> 👀 You can follow what is planned here [https://gitlab.com/smallest-things/scarlet/-/issues/1](https://gitlab.com/smallest-things/scarlet/-/issues/1) and the current activity of the project here [https://gitlab.com/smallest-things/scarlet/-/boards](https://gitlab.com/smallest-things/scarlet/-/boards)


## What is "magic"?

You can add **"Kotlin functions"** to Scarlet (see the `./storage/functions/ directory), and then you can call these functions with a **MQTT client**.

> 🚧 WIP: the functions are compiled at the start of Scarlet

## Build Scarlet

### Build

```bash
mvn clean package
```

## How to use Scarlet

```bash
java -jar target/scarlet-0.0.0-SNAPSHOT-fat.jar
```

> Remarks:
> - default mqtt port is `1883` (use `MQTT_PORT` to change the value)

### 🖐 Use a MQTT client

The examples use `MQTT.js`

You can send three kinds of MQTT messages:

#### json message with call of a function

You have to set the **function name** and the **parameters** like that:

```javascript
// simple text message
client.publish('topic_name', JSON.stringify({function:"hello", params:{name:"bob morane"}}))
```

All the subscribed clients will receive the following Json string with the result of the function:

```javascript
'{"result": {"message": "Hello World"}}'
// where result is a string
```

#### simple text message

```javascript
// simple text message
client.publish('topic_name', "👋 hello world 🌍")
```

All the subscribed clients will receive the following Json string:

```javascript
'{"message": "👋 hello world 🌍"}'
```

#### json message

```javascript
// json message
client.publish('topic_name', JSON.stringify({message:"hello world"}))
```

All the subscribed clients will receive the following Json string:

```javascript
'{"message":"hello world"}'
// where result is a string
```

## Test Scarlet with MQTT.js

- run **Scarlet**: `java -jar target/scarlet-0.0.0-SNAPSHOT-fat.jar`
- create a set of functions: `./create_all_functions.sh`
- See `./testing/mqtt_js`:
  - run `node wait.js`
  - then run `node hello.js`

## Functions persistence

At the first launch, **Scarlet** will create a `./storage` directory with 2 subdirectories:
- `./storage/functions`
- `./storage/events` 🚧 documentation in progress (events are functions triggered by **Scarlet**)
- `./storage/clients` (not used right now)
- `./storage/subscriptions` (not used right now)

All created functions are stored in `./storage/functions` with a filename constructed like that:
```
<function_name>@<version>.<extension>
```

At every start of **Scarlet**, all functions of `./storage` are loaded and compiled.

> - if you have several versions of a function, the last one overrides the others
> - you can override the path `./storage` with this environment variable: `STORAGE_PATH`
