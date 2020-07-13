package garden.bots.scarlet.backend

import garden.bots.scarlet.data.Function
import java.io.File
import java.util.*

val storagePath: String = System.getenv("STORAGE_PATH") ?: "./storage"
val functionsPath = "functions"
val mqttClientsPath = "clients"

val languages: HashMap<String, String> = hashMapOf("js" to "js", "ruby" to "rb", "python" to "py")
val extensions: HashMap<String, String> = hashMapOf("js" to "js", "rb" to "ruby", "py" to "python")

fun initializeStorage() : Result<Boolean> {
  try {
    val storageDirectory = File(storagePath)
    val functionsDirectory = File("${storagePath}/${functionsPath}")
    val clientsDirectory = File("${storagePath}/${mqttClientsPath}")

    storageDirectory.mkdir()
    functionsDirectory.mkdir()
    clientsDirectory.mkdir()

    return Result.success(true)

  } catch (exception : Exception) {
    return Result.failure(exception)
  }

}

fun saveFunction(function: garden.bots.scarlet.data.Function) : Result<garden.bots.scarlet.data.Function>{
  try {
    File("${storagePath}/${functionsPath}/${function.name}@${function.version}.${languages.get(function.language)}").writeText(function.code)
    return Result.success(function)
  } catch (exception: Exception) {
    return Result.failure(exception)
  }
}

fun getAllFunctions() : Result<MutableMap<String, Function>> {
  val functions: MutableMap<String, Function> = HashMap<String, Function>()
  try {
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
          functions.put(functionName, currentFunction)
        }
      }
    }
    return Result.success(functions)
  } catch (exception : Exception) {
    return Result.failure(exception)
  }
}
