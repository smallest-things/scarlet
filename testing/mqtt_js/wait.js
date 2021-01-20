const mqtt = require('mqtt')
const client  = mqtt.connect('mqtt://localhost:1883')

client.on('connect', _ => {
  client.subscribe('buddies', (err) => {
    console.log(err ? err: "🖐️ Welcome")
  })
})

client.on('message',  (topic, message) => {
  console.log(topic, message.toString())
})

