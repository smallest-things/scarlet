import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.core.json.JsonObject
// use the helpers
import garden.bots.scarlet.helpers.helloWorld

fun hello(params: JsonObject): Any {
  //println(vertx)
  return json {
    obj(
      "message" to "👋 hey 😃 welcome to ${params.getString("name")}",
      "greeting" to helloWorld()
    )

  }
}
