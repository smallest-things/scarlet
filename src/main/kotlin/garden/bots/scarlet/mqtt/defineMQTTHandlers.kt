package garden.bots.scarlet.mqtt

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.data.MqttClient
import garden.bots.scarlet.data.MqttSubscription
import garden.bots.scarlet.events.triggerEvent
import garden.bots.scarlet.helpers.executeFunction
import garden.bots.scarlet.helpers.getJsonPayLoad
import garden.bots.scarlet.helpers.isFunctionCall
import io.netty.handler.codec.mqtt.MqttQoS
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.mqtt.MqttAuth
import io.vertx.mqtt.MqttEndpoint
import io.vertx.mqtt.MqttServer

data class MqttParams(val endpoint: MqttEndpoint, val message: io.vertx.mqtt.messages.MqttPublishMessage, val messagePayLoad:String, val mqttSubscriptions: MutableMap<String, MqttSubscription>)

// 🤔 private?
fun createMQTTHandlers(mqttServer: MqttServer, mqttClients:MutableMap<String, MqttClient>, mqttSubscriptions: MutableMap<String, MqttSubscription>, functions: MutableMap<String, Function>, events: MutableMap<String, Function>) {

  mqttServer.endpointHandler { endpoint ->
    // shows main connect info
    println("MQTT client [${endpoint.clientIdentifier()}] request to connect, clean session = ${endpoint.isCleanSession()}")

    /* add mqttclient to the clients list */
    val mqttClient = MqttClient(endpoint.clientIdentifier(), endpoint)
    mqttClients[endpoint.clientIdentifier()] = mqttClient

    /* === 👋 Trigger mqttOnConnect === */
    triggerEvent("mqttOnConnect", mqttClient, events)
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

    // handling requests for subscriptions
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

    // handling incoming published messages
    endpoint.publishHandler { message ->
      val messagePayLoad = message.payload().toString(java.nio.charset.Charset.defaultCharset())
      println("🟠 Just received message [${messagePayLoad}] with QoS [${message.qosLevel()}] on topic [${message.topicName()}]")

      /* --- dispatch messages to all subscribed clients --- */
      val dispatchMessage = { jsonResult: JsonObject ->
        mqttSubscriptions.forEach { (id, mqttSubscription) ->
          when(mqttSubscription.topic==message.topicName() && mqttSubscription.endpoint.isConnected) {
            false -> {
              // nothing to do right now
            }
            true -> {
              mqttSubscription.endpoint.publish(message.topicName(), Buffer.buffer(jsonResult.toString()), message.qosLevel(), false, false)
              // TODO add the other handlers
            }
          }
        }
      }

      /* --- Check and Dispatch --- */
      // check the message payload
      // and then all messages are dispatched with a json format:
      // if it's a function call the payload will be:
      // { result: something }
      // else:
      // { message: something }
      getJsonPayLoad(messagePayLoad)
        .onFailure {
          // this is not a Json payload, this is a simple message
          println("🟦 simple message $messagePayLoad")
          dispatchMessage(json { obj("message" to messagePayLoad) })
        }
        .onSuccess {  jsonObject ->
          // this is a json payload, then check if it's a call of function
          when(isFunctionCall(jsonObject)) {
            false -> {
              println("🟪 json message $jsonObject")
              dispatchMessage(jsonObject)
            }
            true -> { // this is a function call
              executeFunction(jsonObject, functions)
                .onFailure {throwable ->
                  println("🟥 error with jsonObject $jsonObject")
                  dispatchMessage(json { obj("error" to throwable.message) })
                }
                .onSuccess {any ->
                  println("🟧 result of the function call: $any")
                  dispatchMessage(json { obj("result" to any.toString()) })
                }
            }
          }
        }

      /* === 👋 Trigger mqttOnMessage === */
      triggerEvent("mqttOnMessage", MqttParams(endpoint, message, messagePayLoad, mqttSubscriptions), events)
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
}
