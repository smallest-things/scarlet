package garden.bots.scarlet.mqtt

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.data.MqttClient
import garden.bots.scarlet.data.MqttSubscription
import garden.bots.scarlet.events.triggerEvent
import io.vertx.mqtt.MqttAuth
import io.vertx.mqtt.MqttServer

// 🤔 private?
fun createMQTTHandlers(mqttServer: MqttServer, mqttClients:MutableMap<String, MqttClient>, mqttSubscriptions: MutableMap<String, MqttSubscription>, events: MutableMap<String, Function>) {

  mqttServer.endpointHandler { endpoint ->
    // shows main connect info
    println("MQTT client [${endpoint.clientIdentifier()}] request to connect, clean session = ${endpoint.isCleanSession}")

    /* add mqttclient to the clients list */
    val mqttClient = MqttClient(endpoint.clientIdentifier(), endpoint)
    mqttClients[endpoint.clientIdentifier()] = mqttClient

    /* === 👋 Trigger mqttOnConnect === */
    triggerEvent("mqttOnConnect", mqttClient.endpoint, events)
      .onFailure {
        // 🚧
      }
      .onSuccess {
        // 🚧
      }
    /* === end of trigger === */

    when(endpoint.auth()) {
      is MqttAuth -> {
        println("🔑 [username = ${endpoint.auth().username}, password = ${endpoint.auth().password}]")
      }
      else -> {
        // nothing todo right now
      }
    }

    // accept connection from the remote client
    endpoint.accept(false)

    // handling disconnect message
    disconnectHandler(endpoint, mqttSubscriptions, events)

    // handling requests for subscriptions
    subscribeHandler(endpoint, mqttSubscriptions, events)

    // handling incoming published messages
    publishHandler(endpoint, mqttSubscriptions, events)

  }
}
