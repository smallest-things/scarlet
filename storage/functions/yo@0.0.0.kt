import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.core.json.JsonObject

import io.vertx.redis.client.Redis
import garden.bots.scarlet.helpers.redis

fun yo(params: JsonObject): Any {

  // create or get a Redis client
  val cli: Redis = redis("yo")



  //val cli = Redis.createClient(vertx())
  return "😍 Hello World!!! ${params.getString("name")}"
}
