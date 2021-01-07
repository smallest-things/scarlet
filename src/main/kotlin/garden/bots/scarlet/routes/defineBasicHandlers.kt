package garden.bots.scarlet.routes

import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.StaticHandler

fun basicHandlers(router: Router): Router {
  router.route().handler(BodyHandler.create())
  router.route().handler(LoggingHandler())
  router.route("/*").handler(StaticHandler.create().setCachingEnabled(false))
  return router
}
