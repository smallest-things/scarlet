import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import garden.bots.scarlet.data.MqttSubscription

fun mqttOnSubscribe(subscription: MqttSubscription): Unit {
  println("=== 🚀🚀🚀 mqttOnSubscribe ===")
  println(subscription)
  println("${subscription.id} ${subscription.topic}")

}
