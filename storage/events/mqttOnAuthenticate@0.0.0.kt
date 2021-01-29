import io.vertx.mqtt.MqttEndpoint
import io.netty.handler.codec.mqtt.MqttConnectReturnCode
import io.vertx.mqtt.MqttAuth

fun mqttOnAuthenticate(endpoint: MqttEndpoint) : MqttConnectReturnCode {
  println("=== 🚀 mqttAuthenticate ===")

  when(endpoint.auth()) {
    is MqttAuth -> {
      // 🖐 never display a password!!!
      println("🔑 [username = ${endpoint.auth().username}, password = ${endpoint.auth().password}]")

      if (endpoint.auth().username=="thing" && endpoint.auth().password=="secret") {
        return MqttConnectReturnCode.CONNECTION_ACCEPTED
      } else {
        return MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD
      }

    }
    else -> {
      println("🤬 no credential")
      return MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED
    }
  }

}
