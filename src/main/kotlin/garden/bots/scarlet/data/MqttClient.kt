package garden.bots.scarlet.data

import io.vertx.mqtt.MqttEndpoint

data class MqttClient(
  val id:String,
  val topic:String,
  val endpoint: MqttEndpoint
) {}
