package garden.bots.scarlet

import garden.bots.scarlet.backend.initializeStorage
import garden.bots.scarlet.backend.loadAllEventsAndCompile
import garden.bots.scarlet.backend.loadAllFunctionsAndCompile
import garden.bots.scarlet.data.Function
import garden.bots.scarlet.data.MqttClient
import garden.bots.scarlet.data.MqttSubscription
import garden.bots.scarlet.events.triggerEvent
import garden.bots.scarlet.mqtt.createMQTTHandlers
import garden.bots.scarlet.routes.*
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.mqtt.MqttServer
import java.util.logging.Level
import java.util.logging.Logger

class MainVerticle : AbstractVerticle() {

  override fun stop(stopPromise: Promise<Void>) {
    super.stop()
    println("👋 bye")
    stopPromise.complete()
  }

  override fun start(startPromise: Promise<Void>) {
    Logger.getLogger("io.vertx.core.impl.BlockedThreadChecker").setLevel(Level.OFF)

    val httpServer = vertx.createHttpServer()
    val mqttServer = MqttServer.create(vertx)
    val router = Router.router(vertx)

    val functions: MutableMap<String, Function> = HashMap<String, Function>()
    val events: MutableMap<String, Function> = HashMap<String, Function>()

    val mqttSubscriptions: MutableMap<String, MqttSubscription> = HashMap<String, MqttSubscription>()
    val mqttClients: MutableMap<String, MqttClient> = HashMap<String, MqttClient>()

    val httpPort = System.getenv("HTTP_PORT")?.toInt() ?: 8080
    val mqttPort = System.getenv("MQTT_PORT")?.toInt() ?: 1883

    val adminToken = System.getenv("SCARLET_ADMIN_TOKEN") ?: ""
    // use it like that: export SCARLET_ADMIN_TOKEN="tada"; java -jar target/scarlet-0.0.0-SNAPSHOT-fat.jar
    // TODO: implement https and mqtts

    initializeStorage()
      .onFailure {
        println("😡 initializeStorage: ${it.message}")
      }
      .onSuccess {
        loadAllEventsAndCompile(events)
        loadAllFunctionsAndCompile(functions)
        /* === 👋 Trigger initialize === */
        triggerEvent("initialize", json { obj("message" to "initialize") }, events)
          .onFailure {
            println("😡 triggerEvent: initialize | ${it.message}")
          }
          .onSuccess {
            println("🙂 triggerEvent: initialize")
          }
        /* === end of trigger === */
      }

    basicHandlers(router)
    createAddFunctionRoute(router, functions, adminToken)
    createExecuteFunctionRoute(router, functions, adminToken)
    createGetFunctionsRoute(router, functions, adminToken)
    createGetEventsRoute(router, events, adminToken)
    createGetSubscriptionsRoute(router, mqttSubscriptions, adminToken)

    createMQTTHandlers(mqttServer, mqttClients, mqttSubscriptions, functions, events)

    // 🚀 start http server
    httpServer.requestHandler(router)
      .listen(httpPort) { http ->
        when {
          http.failed() -> {
            http.cause().printStackTrace()
            startPromise.fail(http.cause())
          }
          http.succeeded() -> {
            println("🌍 Scarlet -=8< http server started on port $httpPort")

            /* === 👋 Trigger httpStarted === */
            triggerEvent("httpStarted", httpServer, events)
              .onFailure {
                println("😡 triggerEvent: httpStarted | ${it.message}")
              }
              .onSuccess {
                println("🙂 triggerEvent: httpStarted")
              }
            /* === end of trigger === */

            // 🚀 start mqtt server
            mqttServer.listen(mqttPort) { mqtt ->
              when {
                mqtt.failed() -> {
                  mqtt.cause().printStackTrace()
                  startPromise.fail(mqtt.cause())
                }
                mqtt.succeeded() -> {
                  println("📡 Scarlet -=8< mqtt server started on port $mqttPort")

                  /* === 👋 Trigger mqttStarted === */
                  triggerEvent("mqttStarted", mqttServer, events)
                    .onFailure {
                      println("😡 triggerEvent: mqttStarted | ${it.message}")
                    }
                    .onSuccess {
                      println("🙂 triggerEvent: mqttStarted")
                    }
                  /* === end of trigger === */

                  startPromise.complete()
                }
              } // end of when
            } // end of mqtt listen
          } // end of http succeeded
        } // end of when
      }  //end of http listen
  } // end of start()

  companion object {
    /**
     * main function is here only for local debugging purpose
     */
    @JvmStatic
    fun main(args: Array<String>) {
      val vertx: Vertx = Vertx.vertx(VertxOptions())
      vertx.deployVerticle(MainVerticle())
    }
  }
}
