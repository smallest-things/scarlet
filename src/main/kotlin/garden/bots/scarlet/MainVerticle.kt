package garden.bots.scarlet

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.data.MqttClient
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.ext.web.Router
import io.vertx.mqtt.MqttServer

class MainVerticle : AbstractVerticle() {

  override fun stop(stopFuture: Future<Void>) {
    super.stop()
  }

  override fun start(startPromise: Promise<Void>) {
    val httpServer = vertx.createHttpServer()
    val mqttServer = MqttServer.create(vertx)
    val router = Router.router(vertx)

    val functions: MutableMap<String, Function> = HashMap<String, Function>()
    val mqttClients: MutableMap<String, MqttClient> = HashMap<String, MqttClient>()

    val httpPort = System.getenv("`HTTP_PORT`")?.toInt() ?: 8080
    val mqttPort = System.getenv("MQTT_PORT")?.toInt() ?: 1883

    val adminToken = System.getenv("SCARLET_ADMIN_TOKEN") ?: ""
    // use it like that: export SCARLET_ADMIN_TOKEN="tada"; java -jar target/scarlet-0.0.0-SNAPSHOT-fat.jar

    // TODO: implement https and mqtts
    createRoutes(router, functions, adminToken)
    createHandlers(mqttServer, mqttClients, functions)

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
            // 🚀 start mqtt server
            mqttServer.listen(mqttPort) { mqtt ->
              when {
                mqtt.failed() -> {
                  mqtt.cause().printStackTrace()
                  startPromise.fail(mqtt.cause())
                }
                mqtt.succeeded() -> {
                  println("📡 Scarlet -=8< mqtt server started on port $mqttPort")
                  startPromise.complete()
                }
              } // end of when
            } // end of mqtt listen
          } // end of http succeeded
        } // end of when
      }  //end of http listen
  } // end of start()
}
