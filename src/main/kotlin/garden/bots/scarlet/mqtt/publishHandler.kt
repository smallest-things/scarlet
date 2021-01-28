package garden.bots.scarlet.mqtt

import garden.bots.scarlet.data.Function
import io.vertx.mqtt.MqttEndpoint
import garden.bots.scarlet.data.MqttSubscription
import garden.bots.scarlet.events.triggerEvent
import garden.bots.scarlet.functions.executeFunction
import io.netty.handler.codec.mqtt.MqttQoS
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

// TODO: check endpoint.auth() (?)
// handling incoming published messages
fun publishHandler(endpoint: MqttEndpoint,  mqttSubscriptions: MutableMap<String, MqttSubscription>, events: MutableMap<String, Function>) {

  endpoint.publishHandler { message ->
    val messagePayLoad = message.payload().toString(java.nio.charset.Charset.defaultCharset())
    println("🟠 Just received message [${messagePayLoad}] with QoS [${message.qosLevel()}] on topic [${message.topicName()}]")

    /* --- dispatch messages to all subscribed clients --- */
    val dispatchMessage = { textMessage: String ->
      mqttSubscriptions.forEach { (id, mqttSubscription) ->
        when(mqttSubscription.topic==message.topicName() && mqttSubscription.endpoint.isConnected) {
          false -> {
            // nothing to do right now
          }
          true -> {
            mqttSubscription.endpoint.publish(message.topicName(), Buffer.buffer(textMessage), message.qosLevel(), false, false)
          }
        }
      }
    }

    // QUESTION: how to follow the reception of the message

    /* --- Check and Dispatch --- */
    when(message.topicName().startsWith("functions/")) {
      false -> { // this is a simple message
        println("🟦 simple MQTT message $messagePayLoad")
        dispatchMessage(messagePayLoad.toString())
      }
      true -> { // this is a function call
        println("🟩 call of a function $message.topicName() params:  $messagePayLoad")
        executeFunction(message.topicName(), JsonObject(messagePayLoad))
          .onFailure {throwable ->
            println("🟥 error with jsonObject $messagePayLoad")
            dispatchMessage(json { obj("error" to throwable.message) }.toString())
          }
          .onSuccess {any ->
            println("🟢 result of the function call: $any")
            dispatchMessage(json { obj("result" to any.toString()) }.toString())
          }
      }
    }

    /* === 👋 Trigger mqttOnMessage === */
    triggerEvent("mqttOnMessage", message, events)
      .onFailure {
        // 🚧
      }
      .onSuccess {
        // 🚧
      }
    /* === end of trigger === */

    when(message.qosLevel()) {
      MqttQoS.AT_LEAST_ONCE -> {
        endpoint.publishAcknowledge(message.messageId())
      }
      MqttQoS.EXACTLY_ONCE -> {
        endpoint.publishReceived(message.messageId())
      }
      else -> {
        // 🤔 TODO work on QoS
      }
    }
  }.publishReleaseHandler { messageId ->
    endpoint.publishComplete(messageId)
  }
}
