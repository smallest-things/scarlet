function mqttOnMessage(mqttParams) {
  console.log("=== 🚀 mqttOnMessage ===")
  console.log(mqttParams)
  console.log("endPoint:", mqttParams.getEndpoint())
  console.log("message:", mqttParams.getMessage())
  console.log("payload:", mqttParams.getMessagePayLoad())
  console.log("result:", mqttParams.getJsonResult())
  console.log("subscriptions:", mqttParams.getMqttSubscriptions())
}