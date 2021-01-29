package garden.bots.scarlet.helpers

import io.vertx.core.Vertx
import io.vertx.core.VertxOptions

val vertx: Vertx = Vertx.vertx(VertxOptions())

fun vertx(): Vertx {
  return vertx
}
