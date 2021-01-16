import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.core.json.JsonObject

fun hello(params: JsonObject): Any {
  return json {
    obj("message" to "👋 hey 😃 welcome to ${params.getString("name")}")
  }
}
