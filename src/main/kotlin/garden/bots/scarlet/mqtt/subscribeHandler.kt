package garden.bots.scarlet.mqtt

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.data.MqttSubscription
import garden.bots.scarlet.events.triggerEvent
import io.netty.handler.codec.mqtt.MqttQoS
import io.vertx.mqtt.MqttEndpoint

// TODO: check endpoint.auth() (?)
// handling requests for subscriptions
fun subscribeHandler(endpoint: MqttEndpoint, mqttSubscriptions: MutableMap<String, MqttSubscription>, events: MutableMap<String, Function>) {
  endpoint.subscribeHandler { subscribe ->
    val grantedQosLevels = mutableListOf<MqttQoS?>()
    for (s in subscribe.topicSubscriptions()) {
      println("Subscription for ${s.topicName()} with QoS ${s.qualityOfService()}")
      grantedQosLevels.add(s.qualityOfService())

      /* add mqttsubscription to the subscriptions list */
      val subscription = MqttSubscription(endpoint.clientIdentifier(), s.topicName(), endpoint)
      mqttSubscriptions["${endpoint.clientIdentifier()}@${s.topicName()}"] = subscription
      //TODO: api to get the list of the clients

      /* === 👋 Trigger mqttOnSubscribe === */
      triggerEvent("mqttOnSubscribe", subscription, events)
        .onFailure {
          // 🚧
        }
        .onSuccess {
          // 🚧
        }
      /* === end of trigger === */
    }
    // ack the subscriptions request
    endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels)
  }
}
