package garden.bots.scarlet.backend

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.data.MqttClient
import java.io.File
import java.util.*

val storagePath: String = System.getenv("STORAGE_PATH") ?: "./storage"
const val functionsPath = "functions"
const val mqttClientsPath = "clients"
const val eventsPath = "events"

val languages: HashMap<String, String> = hashMapOf("js" to "js", "ruby" to "rb", "python" to "py")
val extensions: HashMap<String, String> = hashMapOf("js" to "js", "rb" to "ruby", "py" to "python")

fun initializeStorage() : Result<Boolean> {
  return try {
    val storageDirectory = File(storagePath)
    val functionsDirectory = File("${storagePath}/${functionsPath}")
    val clientsDirectory = File("${storagePath}/${mqttClientsPath}")
    val eventsDirectory = File("${storagePath}/${eventsPath}")

    storageDirectory.mkdir()
    functionsDirectory.mkdir()
    clientsDirectory.mkdir()
    eventsDirectory.mkdir()

    Result.success(true)
  } catch (exception : Exception) {
    Result.failure(exception)
  }
}

fun saveFunction(function: garden.bots.scarlet.data.Function) : Result<garden.bots.scarlet.data.Function>{
  return try {
    File("${storagePath}/${functionsPath}/${function.name}@${function.version}.${languages.get(function.language)}").writeText(function.code)
    Result.success(function)
  } catch (exception: Exception) {
    Result.failure(exception)
  }
}

fun saveEvent(function: garden.bots.scarlet.data.Function) : Result<garden.bots.scarlet.data.Function>{
  return try {
    File("${storagePath}/${eventsPath}/${function.name}@${function.version}.${languages.get(function.language)}").writeText(function.code)
    Result.success(function)
  } catch (exception: Exception) {
    Result.failure(exception)
  }
}


fun saveMqttClient(mqttClient: garden.bots.scarlet.data.MqttClient) : Result<garden.bots.scarlet.data.MqttClient>{
  return try {
    File("${storagePath}/${mqttClientsPath}/${mqttClient.id}").writeText(mqttClient.toString())
    Result.success(mqttClient)
  } catch (exception: Exception) {
    Result.failure(exception)
  }
}

fun getAllFunctions() : Result<MutableMap<String, Function>> {
  val functions: MutableMap<String, Function> = HashMap<String, Function>()
  return try {
    File("${storagePath}/${functionsPath}").walk().forEach {
      when {
        it.isFile -> {
          val row = it.name.split("@")
          val functionName = row[0]
          val functionExtension = it.canonicalFile.extension
          val functionVersion = row[1].split(functionExtension)[0]
          val functionLanguage = extensions.get(functionExtension)
          val functionCode = it.readText(Charsets.UTF_8)

          val currentFunction: Function = Function(
            functionName,
            functionLanguage.orEmpty(),
            functionCode,
            functionVersion
          )
          functions[functionName] = currentFunction
        }
      }
    }
    Result.success(functions)
  } catch (exception : Exception) {
    Result.failure(exception)
  }
}

// make filters on initialize, httpStarted, mqttStarted
fun getAllEvents() : Result<MutableMap<String, Function>> {
  val events: MutableMap<String, Function> = HashMap<String, Function>()
  return try {
    File("${storagePath}/${eventsPath}").walk().forEach {
      when {
        it.isFile -> {
          val row = it.name.split("@")
          val eventName = row[0]
          val eventExtension = it.canonicalFile.extension
          val eventVersion = row[1].split(eventExtension)[0]
          val eventLanguage = extensions.get(eventExtension)
          val eventCode = it.readText(Charsets.UTF_8)

          val currentFunction: Function = Function(
            eventName,
            eventLanguage.orEmpty(),
            eventCode,
            eventVersion
          )
          events[eventName] = currentFunction
        }
      }
    }
    Result.success(events)
  } catch (exception : Exception) {
    Result.failure(exception)
  }
}

fun getAllMqttClients() : Result<MutableMap<String, MqttClient>> {
  TODO()
}
