package garden.bots.scarlet

import garden.bots.scarlet.data.Function
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

fun getJsonPayLoad(payload: String): Result<JsonObject> {
  return try {
    Result.success(JsonObject(payload))
  } catch (exception: Exception) {
    Result.failure(exception)
  }
}

fun isFunctionCall(jsonObject: JsonObject?): Result<JsonObject?> {
  val functionName = jsonObject?.getString("function") ?: ""
  return when(functionName.isBlank()) {
    true -> {
      Result.failure(Exception("😡 this is not a function call"))
    }
    false -> {
      Result.success(jsonObject)
    }
  }
}

fun executeIfFunctionCall(jsonObject: JsonObject?, functions: MutableMap<String, Function>): Result<String?> {

  return isFunctionCall(jsonObject).let {result ->
    when  {
      result.isFailure -> { // this is not a function call, return the json object
        println("😡 this is not a function call: jsonObject: ===> ${jsonObject}")
        Result.success(jsonObject.toString())
        //Result.failure(Exception("this is not a function cal"))
      }
      result.isSuccess -> { // this is a function call, return the result of the function
        val functionName = jsonObject?.getString("function")
        val functionParameters: JsonObject = jsonObject?.getJsonObject("params") ?: json { obj () }

        invokeFunction(
          functionName,
          functionParameters,
          functions.get(functionName)?.language
        ).let { funcResult ->
          when {
            funcResult.isFailure -> { // execution error
              Result.failure(Exception(funcResult.exceptionOrNull()?.message))
            }
            funcResult.isSuccess -> { // execution is OK
              println("🙂 this is a function call result: ===> ${funcResult}")
              Result.success(funcResult.getOrNull().toString())
            }
            else -> {
              Result.failure(Exception("Huston?"))
            }
          }
        }
      }
      else -> {
        Result.failure(Exception("Huston?"))
      }
    }
  }
}

