const mqtt = require('mqtt')
const client  = mqtt.connect('mqtt://localhost:1883')

client.on('connect', _ => {
  client.subscribe('buddies', (err) => {
    if (!err) {
      client.publish('buddies', JSON.stringify({message:"👋 hello world"}))
      client.publish('buddies', "hey people")
    }
  })
})

client.on('message',  (topic, message) => {
  console.log("👋",JSON.parse(message.toString()))
  client.end()
})
