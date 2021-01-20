const mqtt = require('mqtt')
const client  = mqtt.connect('mqtt://localhost:1883')

client.on('connect', _ => {
  client.subscribe('buddies', (err) => {
    console.log(err ? err: "subscribed to buddies")
  })
  client.subscribe('functions/yo', (err) => {
    console.log(err ? err: "subscribed to functions/yo")
  })
  client.subscribe('functions/hello', (err) => {
    console.log(err ? err: "subscribed to functions/hello")
  })
})

client.on('message',  (topic, message) => {
  console.log(topic, message.toString())
})

