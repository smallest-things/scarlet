package garden.bots.scarlet.functions

import garden.bots.scarlet.languages.invokeFunction
import io.vertx.core.json.JsonObject

fun executeFunction(topicName: String,  jsonObjectParameters: JsonObject): Result<Any?> {
  // this is a function call, return the result of the function
  val functionName = topicName.split("functions/")[1]
  return invokeFunction(
    functionName,
    jsonObjectParameters
  ).onFailure { /* === 😡 Failure === */
    return Result.failure(it) // Exception(it.message)
  }.onSuccess { /* === 🙂 Success === */
    return Result.success(it.toString())
  }
}
