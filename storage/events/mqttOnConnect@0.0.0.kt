import io.vertx.mqtt.MqttEndpoint

fun mqttOnConnect(endpoint: MqttEndpoint) {
  println("=== 🚀 mqttOnConnect ===")
  println(endpoint)
}
