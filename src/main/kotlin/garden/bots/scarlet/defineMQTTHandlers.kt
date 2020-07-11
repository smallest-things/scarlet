package garden.bots.scarlet

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.data.MqttClient
import io.netty.handler.codec.mqtt.MqttQoS
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.mqtt.MqttAuth
import io.vertx.mqtt.MqttServer

fun createHandlers(mqttServer: MqttServer, mqttClients: MutableMap<String, MqttClient>, functions: MutableMap<String, Function>) {
  mqttServer.endpointHandler { endpoint ->
    // shows main connect info
    println("MQTT client [${endpoint.clientIdentifier()}] request to connect, clean session = ${endpoint.isCleanSession()}")

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
    }

    // handling requests for subscriptions
    endpoint.subscribeHandler { subscribe ->
      val grantedQosLevels = mutableListOf<MqttQoS?>()
      for (s in subscribe.topicSubscriptions()) {
        println("Subscription for ${s.topicName()} with QoS ${s.qualityOfService()}")
        grantedQosLevels.add(s.qualityOfService())

        mqttClients.put("${endpoint.clientIdentifier()}-", MqttClient(endpoint.clientIdentifier(), s.topicName(), endpoint))
        //TODO: api to get the list of the clients

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

      }
      // dispatch messages to all subscribed clients
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
