package garden.bots.scarlet.helpers

import garden.bots.scarlet.languages.invokeFunction
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

fun getJsonPayLoad(payload: String): Result<JsonObject> {
  return Result.runCatching { JsonObject(payload) }
}

fun isFunctionCall(jsonObject: JsonObject): Boolean {
  return !jsonObject.getString("function").isNullOrEmpty()
}

fun executeFunction(jsonObject: JsonObject?): Result<Any?> {
  // this is a function call, return the result of the function
  val functionName = jsonObject?.getString("function")
  val functionParameters: JsonObject = jsonObject?.getJsonObject("params") ?: json { obj () }
  return invokeFunction(
    functionName,
    functionParameters
  ).onFailure { /* === 😡 Failure === */
    return Result.failure(it) // Exception(it.message)
  }.onSuccess { /* === 🙂 Success === */
    println("🙂 this is a function call result: ===> $it")
    Result.success(it.toString())
  }
}



