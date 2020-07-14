package garden.bots.scarlet.mqtt

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.data.MqttClient
import garden.bots.scarlet.data.MqttSubscription
import garden.bots.scarlet.events.triggerEvent
import garden.bots.scarlet.helpers.executeIfFunctionCall
import garden.bots.scarlet.helpers.getJsonPayLoad
import io.netty.handler.codec.mqtt.MqttQoS
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.mqtt.MqttAuth
import io.vertx.mqtt.MqttEndpoint
import io.vertx.mqtt.MqttServer

data class MqttParams(val endpoint: MqttEndpoint, val message: io.vertx.mqtt.messages.MqttPublishMessage, val messagePayLoad:String, val jsonResult: JsonObject, val mqttSubscriptions: MutableMap<String, MqttSubscription>)

fun createMQTTHandlers(mqttServer: MqttServer, mqttClients:MutableMap<String, MqttClient>, mqttSubscriptions: MutableMap<String, MqttSubscription>, functions: MutableMap<String, Function>, events: MutableMap<String, Function>) {
  mqttServer.endpointHandler { endpoint ->
    // shows main connect info
    println("MQTT client [${endpoint.clientIdentifier()}] request to connect, clean session = ${endpoint.isCleanSession()}")

    /* add mqttclient to the clients list */
    val mqttClient = MqttClient(endpoint.clientIdentifier(), endpoint)
    mqttClients[endpoint.clientIdentifier()] = mqttClient


    /* === 👋 Trigger mqttOnConnect === */
    triggerEvent("mqttOnConnect", mqttClient, events).let {
      when {
        it.isFailure -> {}
        it.isSuccess -> {}
      }
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
      triggerEvent("mqttOnDisConnect", endpoint, events).let {
        when {
          it.isFailure -> {}
          it.isSuccess -> {}
        }
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
        triggerEvent("mqttOnSubscribe", subscription, events).let {
          when {
            it.isFailure -> {}
            it.isSuccess -> {}
          }
        }
        /* === end of trigger === */
      }
      // ack the subscriptions request
      endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels)
    }

    // handling incoming published messages
    endpoint.publishHandler { message ->
      val messagePayLoad = message.payload().toString(java.nio.charset.Charset.defaultCharset())
      println("Just received message [${messagePayLoad}] with QoS [${message.qosLevel()}] on topic [${message.topicName()}]")

      val jsonResult: JsonObject = getJsonPayLoad(messagePayLoad).let { result ->
        when {
          result.isFailure -> { // this is not a Json payload, transform the message to Json Object
            json {
              obj("message" to messagePayLoad)
            }
          }
          result.isSuccess -> { // this is a payload message
            executeIfFunctionCall(result.getOrNull(), functions).let { executeResult ->
              when {
                executeResult.isFailure -> { // execution error
                  json {
                    obj("error" to executeResult.exceptionOrNull()?.message)
                  }
                }
                executeResult.isSuccess -> { // execution result
                  println("🖐 result of the function call: $executeResult")
                  json {
                    obj("result" to executeResult.getOrNull().toString())
                  }
                }
                else -> {
                  json {
                    obj("else_error" to "🥶 Huston?")
                  }
                }
              }
            }
          }
          else -> {
            json {
              obj("else_error" to "🥶 Huston?")
            }
          }
        }
      } // End of getJsonPayLoad

      /* === 👋 Trigger mqttOnMessage === */
      triggerEvent("mqttOnMessage", MqttParams(endpoint, message, messagePayLoad, jsonResult, mqttSubscriptions), events).let {
        when {
          it.isFailure -> {}
          it.isSuccess -> {}
        }
      }
      /* === end of trigger === */

      /* --- dispatch messages to all subscribed clients --- */
      mqttSubscriptions.forEach { id, mqttSubscription ->
        when(mqttSubscription.topic==message.topicName() && mqttSubscription.endpoint.isConnected()) {
          false -> {
            // nothing to do right now
          }
          true -> {
            mqttSubscription.endpoint.publish(message.topicName(), Buffer.buffer(jsonResult.toString()), message.qosLevel(), false, false)
            // TODO add the other handers
          }
        }
      }

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
