package garden.bots.scarlet.mqtt

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.data.MqttClient
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

data class mqttParams(val endpoint: MqttEndpoint, val message: io.vertx.mqtt.messages.MqttPublishMessage, val messagePayLoad:String, val jsonResult: JsonObject, val mqttClients: MutableMap<String, MqttClient>)


fun createMQTTHandlers(mqttServer: MqttServer, mqttClients: MutableMap<String, MqttClient>, functions: MutableMap<String, Function>, events: MutableMap<String, Function>) {
  mqttServer.endpointHandler { endpoint ->
    // shows main connect info
    println("MQTT client [${endpoint.clientIdentifier()}] request to connect, clean session = ${endpoint.isCleanSession()}")

    /* === 👋 Trigger mqttOnConnect === */
    triggerEvent("mqttOnConnect", endpoint, events).let {
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

        // 👋👋👋 a mqtt client can have several topics
        // so, change the id: identifier + topic or change the structure of the mqtt client
        mqttClients["${endpoint.clientIdentifier()}-"] = MqttClient(endpoint.clientIdentifier(), s.topicName(), endpoint)
        //TODO: api to get the list of the clients

        /* === 👋 Trigger mqttOnSubscribe === */
        triggerEvent("mqttOnSubscribe", endpoint, events).let {
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
      triggerEvent("mqttOnMessage", mqttParams(endpoint, message, messagePayLoad, jsonResult, mqttClients), events).let {
        when {
          it.isFailure -> {}
          it.isSuccess -> {}
        }
      }
      /* === end of trigger === */

      /* --- dispatch messages to all subscribed clients --- */
      mqttClients.forEach { id, mqttClient ->
        when(mqttClient.topic==message.topicName() && mqttClient.endpoint.isConnected()) {
          false -> {
            // nothing to do right now
          }
          true -> {
            mqttClient.endpoint.publish(message.topicName(), Buffer.buffer(jsonResult.toString()), message.qosLevel(), false, false)
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
