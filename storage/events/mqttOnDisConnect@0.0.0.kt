import io.vertx.mqtt.MqttEndpoint

fun mqttOnDisConnect(endpoint: MqttEndpoint) {
  println("=== 🚀 mqttOnDisConnect ===")
  println(endpoint)
}
