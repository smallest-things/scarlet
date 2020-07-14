package garden.bot.scarlet

import garden.bots.scarlet.backend.getAllFunctions
import garden.bots.scarlet.backend.initializeStorage
import garden.bots.scarlet.backend.saveEvent
import garden.bots.scarlet.backend.saveFunction
import garden.bots.scarlet.data.Function
import org.junit.jupiter.api.Test

class TestBackend {
  @Test
  fun step_01_initializeLocalStorage() {
    initializeStorage().let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          assert(true)
        }
      }
    }
  }

  @Test
  fun step_02_addFunction() {
    val defaultFunctionCode = """
    def greetings(params):
        return "Name is " + params.getString("name")
    """.trimIndent()

    val currentFunction: Function = Function(
      "greetings",
      "python",
      defaultFunctionCode,
      "6.6.6"
    )

    saveFunction(currentFunction).let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          println("step_02_addFunction: ${result}")
          assert(true)
        }
      }
    }
  }

  @Test
  fun step_03_addFunction() {
    val defaultFunctionCode = """
      function wow(params) {
        return {
          message: "👋 Hello World 🌍",
          author: "John Doe"
        }
      }
    """.trimIndent()

    val currentFunction: Function = Function(
      "wow",
      "js",
      defaultFunctionCode,
      "6.6.6"
    )

    saveFunction(currentFunction).let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          println("step_03_addFunction: ${result}")
          assert(true)
        }
      }
    }
  }

  //TODO: add compilation
  @Test
  fun step_04_getAllfunctions() {
    getAllFunctions().let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          println("step_04_getAllfunctions: ${result}")
          assert(true)
        }
      }
    }
  }

  @Test
  fun step_05_addInitializeEvent() {
    val defaultFunctionCode = """
      function initialize(params) {
        console.log("=== 🚀 initialize ===")
        console.log(params)
      }
    """.trimIndent()

    val currentEvent: Function = Function(
      "initialize",
      "js",
      defaultFunctionCode,
      "0.0.0"
    )

    saveEvent(currentEvent).let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          println("step_05_addInitializeEvent: ${result}")
          assert(true)
        }
      }
    }
  }

  @Test
  fun step_06_addHttpStartedEvent() {
    val defaultFunctionCode = """
      function httpStarted(httpServer) {
        console.log("=== 🚀 httpStarted ===")
        console.log(httpServer)
      }
    """.trimIndent()

    val currentEvent: Function = Function(
      "httpStarted",
      "js",
      defaultFunctionCode,
      "0.0.0"
    )

    saveEvent(currentEvent).let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          println("step_06_addHttpStartedEvent: ${result}")
          assert(true)
        }
      }
    }
  }

  @Test
  fun step_07_addMqttStartedEvent() {
    val defaultFunctionCode = """
      function mqttStarted(mqttServer) {
        console.log("=== 🚀 mqttStarted ===")
        console.log(mqttServer)
      }
    """.trimIndent()

    val currentEvent: Function = Function(
      "mqttStarted",
      "js",
      defaultFunctionCode,
      "0.0.0"
    )

    saveEvent(currentEvent).let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          println("step_07_addMqttStartedEvent: ${result}")
          assert(true)
        }
      }
    }
  }

  @Test
  fun step_08_addMqttOnConnect() {
    val defaultFunctionCode = """
      function mqttOnConnect(endpoint) {
        console.log("=== 🚀 mqttOnConnect ===")
        console.log(endpoint)
      }
    """.trimIndent()

    val currentEvent: Function = Function(
      "mqttOnConnect",
      "js",
      defaultFunctionCode,
      "0.0.0"
    )

    saveEvent(currentEvent).let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          println("step_08_addMqttOnConnect: ${result}")
          assert(true)
        }
      }
    }
  }

  @Test
  fun step_09_addMqttOnDisConnect() {
    val defaultFunctionCode = """
      function mqttOnDisConnect(endpoint) {
        console.log("=== 🚀 mqttOnDisConnect ===")
        console.log(endpoint)
      }
    """.trimIndent()

    val currentEvent: Function = Function(
      "mqttOnDisConnect",
      "js",
      defaultFunctionCode,
      "0.0.0"
    )

    saveEvent(currentEvent).let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          println("step_09_addMqttOnDisConnect: ${result}")
          assert(true)
        }
      }
    }
  }

  @Test
  fun step_10_addMqttOnSubscribe() {
    val defaultFunctionCode = """
      function mqttOnSubscribe(endpoint) {
        console.log("=== 🚀 mqttOnSubscribe ===")
        console.log(endpoint)
      }
    """.trimIndent()

    val currentEvent: Function = Function(
      "mqttOnSubscribe",
      "js",
      defaultFunctionCode,
      "0.0.0"
    )

    saveEvent(currentEvent).let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          println("step_10_addMqttOnSubscribe: ${result}")
          assert(true)
        }
      }
    }
  }

  @Test
  fun step_11_addMqttOnMessage() {
    val defaultFunctionCode = """
      function mqttOnMessage(mqttParams) {
        console.log("=== 🚀 mqttOnMessage ===")
        console.log(mqttParams)
        console.log(mqttParams.endpoint)
        console.log(mqttParams.message)
        console.log(mqttParams.messagePayLoad)
        console.log(mqttParams.jsonResult)
        console.log(mqttParams.mqttClients)
      }
    """.trimIndent()

    val currentEvent: Function = Function(
      "mqttOnMessage",
      "js",
      defaultFunctionCode,
      "0.0.0"
    )

    saveEvent(currentEvent).let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          println("step_11_addMqttOnMessage: ${result}")
          assert(true)
        }
      }
    }
  }


}
