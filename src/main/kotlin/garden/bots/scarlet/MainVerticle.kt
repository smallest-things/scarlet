package garden.bots.scarlet

import garden.bots.scarlet.backend.initializeStorage
import garden.bots.scarlet.backend.loadAllEventsAndCompile
import garden.bots.scarlet.backend.loadAllFunctionsAndCompile
import garden.bots.scarlet.data.Function
import garden.bots.scarlet.data.MqttClient
import garden.bots.scarlet.data.MqttSubscription
import garden.bots.scarlet.events.triggerEvent
import garden.bots.scarlet.mqtt.createMQTTHandlers
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.net.PemKeyCertOptions
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.mqtt.MqttServer
import io.vertx.mqtt.MqttServerOptions
import java.util.logging.Level
import java.util.logging.Logger

class MainVerticle : AbstractVerticle() {


  override fun stop(stopPromise: Promise<Void>) {
    super.stop()
    stopPromise.complete()
  }

  override fun start(startPromise: Promise<Void>) {
    Logger.getLogger("io.vertx.core.impl.BlockedThreadChecker").level = Level.OFF

    val functions: MutableMap<String, Function> = HashMap()
    val events: MutableMap<String, Function> = HashMap()
    val mqttSubscriptions: MutableMap<String, MqttSubscription> = HashMap()
    val mqttClients: MutableMap<String, MqttClient> = HashMap()
    val mqttPort = System.getenv("MQTT_PORT")?.toInt() ?: 1883

    val mqttOptions = MqttServerOptions().setPort(mqttPort)

    System.getenv("MQTT_KEY")?.takeIf { mqttKey ->
      System.getenv("MQTT_CERT")?.takeIf { mqttCert ->
        //TODO check if files exist
        mqttOptions.setKeyCertOptions(PemKeyCertOptions()
          .setKeyPath(mqttKey)
          .setCertPath(mqttCert)
        ).isSsl = true
        true
      }
      true
    }.let {
      //TODO: display message
    }

    val mqttServer = MqttServer.create(vertx, mqttOptions)

    val adminToken = System.getenv("SCARLET_ADMIN_TOKEN") ?: ""
    // use it like that: export SCARLET_ADMIN_TOKEN="tada"; java -jar target/scarlet-0.0.0-SNAPSHOT-fat.jar

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
            println("😶 triggerEvent: initialize | ${it.message}")
          }
          .onSuccess {
            println("🙂 triggerEvent: initialize")
          }
        /* === end of trigger === */
      }

    createMQTTHandlers(mqttServer, mqttClients, mqttSubscriptions, events)

    // 🚀 start mqtt server
    mqttServer.listen() { mqtt ->
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
              println("😶 triggerEvent: mqttStarted | ${it.message}")
            }
            .onSuccess {
              println("🙂 triggerEvent: mqttStarted")
            }
          /* === end of trigger === */
          startPromise.complete()
        }
      } // end of when
    } // end of mqtt listen
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
