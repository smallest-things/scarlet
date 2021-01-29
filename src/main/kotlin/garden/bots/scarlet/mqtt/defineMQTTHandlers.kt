package garden.bots.scarlet.mqtt

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.data.MqttClient
import garden.bots.scarlet.data.MqttSubscription
import garden.bots.scarlet.events.triggerEvent
import io.netty.handler.codec.mqtt.MqttConnectReturnCode
import io.vertx.mqtt.MqttAuth
import io.vertx.mqtt.MqttEndpoint
import io.vertx.mqtt.MqttServer

fun resumeHandlersCreation(endpoint: MqttEndpoint, mqttClients:MutableMap<String, MqttClient>, mqttSubscriptions: MutableMap<String, MqttSubscription>, events: MutableMap<String, Function>) {
  // handling disconnect message
  disconnectHandler(endpoint, mqttClients, mqttSubscriptions, events)

  // handling requests for subscriptions
  subscribeHandler(endpoint, mqttSubscriptions, events)

  // handling incoming published messages
  publishHandler(endpoint, mqttSubscriptions, events)
}

// 🤔 private?
fun createMQTTHandlers(mqttServer: MqttServer, mqttClients:MutableMap<String, MqttClient>, mqttSubscriptions: MutableMap<String, MqttSubscription>, events: MutableMap<String, Function>) {

  mqttServer.endpointHandler { endpoint ->
    // shows main connect info
    println("🦚 MQTT client [${endpoint.clientIdentifier()}] request to connect, clean session = ${endpoint.isCleanSession}")

    /* add mqttclient to the clients list */
    val mqttClient = MqttClient(endpoint.clientIdentifier(), endpoint)
    mqttClients[endpoint.clientIdentifier()] = mqttClient

    /* === 👋 Trigger mqttOnConnect === */
    //triggerEvent("mqttOnConnect", mqttClient.endpoint, events)
    triggerEvent("mqttOnConnect", endpoint, events)
      .onFailure {
        // 🚧
      }
      .onSuccess {
        // 🚧
      }
    /* === end of trigger === */
    println("😶 this is a connection without authentication: ${endpoint.clientIdentifier()}")
    val clients = mqttClients.map { entry -> entry.value.id }
    println("👨‍👩‍👧‍👦 mqttClients: $clients")

    // accept connection from the remote client
    // TODO: what is the meaning of the parameter of accept
    endpoint.accept(false)

    resumeHandlersCreation(endpoint, mqttClients, mqttSubscriptions, events)

  }

}

fun createMQTTHandlersWithAuthentication(mqttServer: MqttServer, mqttClients:MutableMap<String, MqttClient>, mqttSubscriptions: MutableMap<String, MqttSubscription>, events: MutableMap<String, Function>) {

  mqttServer.endpointHandler { endpoint ->
    // shows main connect info
    println("🐝 MQTT client [${endpoint.clientIdentifier()}] request to connect, clean session = ${endpoint.isCleanSession}")

    /* === 👋 Trigger mqttOnConnect === */
    //triggerEvent("mqttOnConnect", mqttClient.endpoint, events)
    triggerEvent("mqttOnConnect", endpoint, events)
      .onFailure {
        // 🚧
      }
      .onSuccess {
        // 🚧
      }
    /* === end of trigger === */

    triggerEvent("mqttOnAuthenticate", endpoint, events)
      .onFailure {
        // 🚧
        endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED)
      }
      .onSuccess {
        // 🚧
        println("Result of authentication: $it")
        when (it) {
          is MqttConnectReturnCode -> {
            when(it) {
              MqttConnectReturnCode.CONNECTION_ACCEPTED -> {
                // All good!
                println("🙂 Welcome ${endpoint.auth().username}")
                /* add mqttclient to the clients list */
                val mqttClient = MqttClient(endpoint.clientIdentifier(), endpoint)
                mqttClients[endpoint.clientIdentifier()] = mqttClient

                val clients = mqttClients.map { entry -> entry.value.id }
                println("👨‍👩‍👧‍👦 mqttClients: $clients")

                // accept connection from the remote client
                endpoint.accept(false)

                resumeHandlersCreation(endpoint, mqttClients, mqttSubscriptions, events)

              }
              else -> {
                endpoint.reject(it)
              }
            }

          }
          else -> {
            endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED)
            println("😡🖐 you should check that mqttOnAuthenticate returns a MqttConnectReturnCode")
          }
        }

      }
    /* === end of trigger === */

  }
}
