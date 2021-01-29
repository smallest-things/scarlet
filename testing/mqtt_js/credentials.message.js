const mqtt = require('mqtt')

const client  = mqtt.connect({
  port: 1883,
  username: "thing",
  password: "secret",
  host: "localhost",
  protocol: "mqtt"
})


client.on('connect', _ => {
  client.subscribe('buddies', (err) => {
    if (!err) {
      client.publish('buddies', "hey people")
      client.publish('buddies', "hello people")
      client.publish('buddies', "morgen")
    }

  })
})

client.on('message',  (topic, message) => {
  console.log("👋", message.toString())
  client.end() // disconnect handler is called
})
