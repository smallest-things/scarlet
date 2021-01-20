package garden.bots.scarlet.helpers

import io.vertx.core.json.JsonObject

fun isJsonPayLoad(payload: String): Result<JsonObject> {
  return Result.runCatching { JsonObject(payload) }
}




