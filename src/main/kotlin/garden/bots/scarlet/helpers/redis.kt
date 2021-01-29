package garden.bots.scarlet.helpers

import io.vertx.redis.client.Redis

var redisClients : MutableMap<String, Redis> = mutableMapOf<String, Redis>()

fun redis(name: String) : Redis {
  return when(redisClients[name]) {
    is Redis -> {
      println("🌼 the Redis client [$name] already exists")
      redisClients[name]!!
    }
    else -> {
      println("🌸 create a Redis client [$name]")
      redisClients[name] = Redis.createClient(vertx())
      redisClients[name]!!
    }
  }
}

//TODO: remove a Redis client

fun redis(name: String, cx: String) : Redis {
  TODO()
}


