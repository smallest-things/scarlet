const mqtt = require('mqtt')
const path = require('path')
const fs = require('fs')
let KEY = fs.readFileSync(path.join(__dirname, '..', '..', 'certs', 'mqtt.devsecops.run.key'))
let CERT = fs.readFileSync(path.join(__dirname, '..', '..', 'certs', 'mqtt.devsecops.run.crt'))

const client  = mqtt.connect({
  port: 8883,
  key: KEY,
  cert: CERT,
  rejectUnauthorized: false,
  host: "localhost",
  protocol: "mqtts"
})

client.on('connect', _ => {
  client.subscribe('buddies', (err) => {
    if (!err) {
      client.publish('buddies', "hey people")
    }
  })
})

client.on('message',  (topic, message) => {
  console.log("👋", message.toString())
  client.end()
})
