import io.vertx.mqtt.MqttServer

fun mqttStarted(mqttServer: MqttServer) {
  println("=== 🚀 mqttStarted ===")
  println(mqttServer)
}
