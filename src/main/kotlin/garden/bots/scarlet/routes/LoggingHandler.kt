package garden.bots.scarlet.routes

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class LoggingHandler : Handler<RoutingContext> {

  private val logger: Logger = Logger.getLogger(this::class.java.name)

  override fun handle(context: RoutingContext) {
    val chrono = Chrono()
    val routeUri = context.request().uri()
    logger.info("BEGIN : $routeUri")
    context.response().endHandler {
      chrono.stop()
      logger.info("END : ${routeUri} - time ${chrono.getTimeSpentMs()} ms")
    }
    context.next()
  }

  class Chrono {
    private var startTime: Long = 0
    private var stopTime: Long = 0

    fun start() {
      startTime = System.nanoTime()
    }

    fun stop() {
      stopTime = System.nanoTime()
    }

    fun getTimeSpentMs(): Long {
      return TimeUnit.MILLISECONDS.convert(stopTime - startTime, TimeUnit.NANOSECONDS)
    }

  }

}
