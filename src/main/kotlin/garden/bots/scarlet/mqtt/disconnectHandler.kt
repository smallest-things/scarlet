package garden.bots.scarlet.mqtt

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.data.MqttSubscription
import garden.bots.scarlet.events.triggerEvent
import io.vertx.mqtt.MqttEndpoint

// handling disconnect message
fun disconnectHandler(endpoint: MqttEndpoint, mqttSubscriptions: MutableMap<String, MqttSubscription>, events: MutableMap<String, Function>) {
  endpoint.disconnectHandler {
    println("Received disconnect from client")
    //TODO: remove from the list

    /* === 👋 Trigger mqttOnDisConnect === */
    triggerEvent("mqttOnDisConnect", endpoint, events)
      .onFailure {
        // 🚧
      }
      .onSuccess {
        // 🚧
      }
    /* === end of trigger === */
  }
}
