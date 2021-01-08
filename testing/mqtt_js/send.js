const mqtt = require('mqtt')
const client  = mqtt.connect('mqtt://localhost:1883')

client.on('connect', _ => {
  client.subscribe('buddies', (err) => {
    if (!err) {
      client.publish('buddies', JSON.stringify({function:"hello", params:{name:"bob"}}))
      client.publish('buddies', JSON.stringify({message:"👋 hello world"}))
      client.publish('buddies', "👋")
      client.publish('buddies', "hey people")
      client.publish('buddies', JSON.stringify({function:"ola", params:{name:"jane"}}))
      client.publish('buddies', JSON.stringify({function:"plop", params:{name:"john"}}))
    }
  })
})

client.on('message',  (topic, message) => {
  console.log("👋",JSON.parse(message.toString()))
  client.end()
})
