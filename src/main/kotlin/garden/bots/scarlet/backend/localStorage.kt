package garden.bots.scarlet.backend

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.data.MqttClient
import garden.bots.scarlet.data.MqttSubscription
import java.io.File
import java.util.*

val storagePath: String = System.getenv("STORAGE_PATH") ?: "./storage"
const val functionsPath = "functions"
const val mqttClientsPath = "clients"
const val mqttSubscriptionsPath = "subscriptions"
const val eventsPath = "events"

fun initializeStorage() : Result<Boolean> {
  return Result.runCatching {
    val storageDirectory = File(storagePath)
    val functionsDirectory = File("${storagePath}/${functionsPath}")
    val clientsDirectory = File("${storagePath}/${mqttClientsPath}")
    val subscriptionsDirectory = File("${storagePath}/${mqttSubscriptionsPath}")
    val eventsDirectory = File("${storagePath}/${eventsPath}")

    storageDirectory.mkdir()
    functionsDirectory.mkdir()
    clientsDirectory.mkdir()
    eventsDirectory.mkdir()
    subscriptionsDirectory.mkdir()
    true
  }

}

fun saveFunction(function: Function) : Result<Function>{
  return Result.runCatching {
    File("${storagePath}/${functionsPath}/${function.name}@${function.version}.kt").writeText(function.code)
    function
  }
}

fun saveEvent(function: Function) : Result<Function>{
  return Result.runCatching {
    File("${storagePath}/${eventsPath}/${function.name}@${function.version}.kt").writeText(function.code)
    function
  }
}

fun saveMqttClient(mqttClient: MqttClient) : Result<MqttClient>{
  return Result.runCatching {
    File("${storagePath}/${mqttClientsPath}/${mqttClient.id}").writeText(mqttClient.toString())
    mqttClient
  }
}

fun saveMqttSubscription(mqttSubscription: MqttSubscription) : Result<MqttSubscription>{
  return Result.runCatching {
    File("${storagePath}/${mqttSubscriptionsPath}/${mqttSubscription.id}").writeText(mqttSubscription.toString())
    mqttSubscription
  }
}

fun getAllFunctions() : Result<MutableMap<String, Function>> {
  val functions: MutableMap<String, Function> = HashMap()
  return Result.runCatching {
    File("${storagePath}/${functionsPath}").walk().forEach {
      when {
        it.isFile -> {
          val row = it.name.split("@")
          val functionName = row[0]
          val functionExtension = it.canonicalFile.extension
          val functionVersion = row[1].split(functionExtension)[0]
          val functionLanguage = "kotlin"
          val functionCode = it.readText(Charsets.UTF_8)

          val currentFunction = Function(
            functionName,
            functionLanguage.orEmpty(),
            functionCode,
            functionVersion
          )
          functions[functionName] = currentFunction
        }
      }
    }
    functions
  }
}

// make filters on initialize, httpStarted, mqttStarted
fun getAllEvents() : Result<MutableMap<String, Function>> {
  val events: MutableMap<String, Function> = HashMap()
  return Result.runCatching {
    File("${storagePath}/${eventsPath}").walk().forEach {
      when {
        it.isFile -> {
          val row = it.name.split("@")
          val eventName = row[0]
          val eventExtension = it.canonicalFile.extension
          val eventVersion = row[1].split(eventExtension)[0]
          val eventLanguage = "kotlin"
          val eventCode = it.readText(Charsets.UTF_8)

          val currentFunction = Function(
            eventName,
            eventLanguage.orEmpty(),
            eventCode,
            eventVersion
          )
          events[eventName] = currentFunction
        }
      }
    }
    events
  }
}

fun getAllMqttClients() : Result<MutableMap<String, MqttClient>> {
  TODO()
}

fun getAllMqttSubscriptions() : Result<MutableMap<String, MqttSubscription>> {
  TODO()
}
