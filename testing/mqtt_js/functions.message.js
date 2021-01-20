const mqtt = require('mqtt')
const client  = mqtt.connect('mqtt://localhost:1883')

client.on('connect', _ => {
  client.subscribe('functions/hello', (err) => {
    if (!err) {
      //client.publish('buddies', JSON.stringify({function:"hello", params:{name:"bob"}}))
      //client.publish('buddies', JSON.stringify({function:"yo", params:{name:"bobby"}}))

      client.publish('functions/hello', JSON.stringify({name:"bob"}))

    }
  })

  client.subscribe('functions/yo', (err) => {
    if (!err) {
      //client.publish('buddies', JSON.stringify({function:"hello", params:{name:"bob"}}))
      //client.publish('buddies', JSON.stringify({function:"yo", params:{name:"bobby"}}))

      client.publish('functions/yo', JSON.stringify({name:"bobby"}))

    }
  })

})

client.on('message',  (topic, message) => {
  console.log("👋",JSON.parse(message.toString()))
  client.end()
})
