package garden.bots.scarlet.mqtt

import garden.bots.scarlet.data.MqttSubscription
import io.netty.handler.codec.mqtt.MqttQoS
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject

fun sendMessage(mqttSubscription: MqttSubscription, topicName: String, jsonMessage: JsonObject) {
  mqttSubscription.endpoint.publish(topicName, Buffer.buffer(jsonMessage.toString()), MqttQoS.AT_LEAST_ONCE, false, false)
}

fun dispatchToAll(mqttSubscriptions: MutableMap<String, MqttSubscription>, topicName: String, jsonMessage: JsonObject) {
  mqttSubscriptions.forEach { (id, mqttSubscription) ->
    when(mqttSubscription.topic==topicName && mqttSubscription.endpoint.isConnected) {
      false -> {
        // nothing to do right now
      }
      true -> {
        mqttSubscription.endpoint.publish(topicName, Buffer.buffer(jsonMessage.toString()), MqttQoS.AT_LEAST_ONCE, false, false)
        // TODO: work on MqttQoS
      }
    }
  }
}
