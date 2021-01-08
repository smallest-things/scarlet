import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.ext.web.RoutingContext
import io.vertx.core.json.JsonObject

fun yo(params: JsonObject): Any {
  return "😍 Hello World!!! ${params.getString("name")}"
}
