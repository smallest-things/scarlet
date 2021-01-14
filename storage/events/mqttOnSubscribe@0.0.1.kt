import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import garden.bots.scarlet.data.MqttSubscription
import garden.bots.scarlet.mqtt.sendMessage

fun mqttOnSubscribe(subscription: MqttSubscription) {
  println("=== 🚀🚀🚀 mqttOnSubscribe ===")
  println(subscription)
  println("${subscription.id} ${subscription.topic}")

  try {
    sendMessage(
      subscription,
      subscription.topic,
      json { obj("message" to "👋 hey 😃 welcome to ${subscription.topic}") }
    )
  } catch(exception: Exception) {
    println("😡 issue with mqtt subscription: ${exception}")
  }
}
