import io.vertx.mqtt.messages.MqttPublishMessage

fun mqttOnMessage(message: MqttPublishMessage) {
  println(message)
}
