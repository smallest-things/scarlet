package garden.bots.scarlet.backend

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.languages.compileFunction

//TODO return a Result 🤔
fun loadAllEventsAndCompile(events: MutableMap<String, Function>) {

  getAllEvents()
    .onFailure {throwable ->
      println(throwable.message)
    }
    .onSuccess { mutableMap ->
      mutableMap.forEach { (key, value) ->
        events[key] = value
        val currentEvent: Function = value

        compileFunction(currentEvent.code)
          .onFailure { throwable ->
            /* === 😡 Failure === */
            println(throwable.message)
          }
          .onSuccess {
            /* === 🙂 Success === */
            println("event ${currentEvent.name} is compiled")
          }
      }
    }

}
