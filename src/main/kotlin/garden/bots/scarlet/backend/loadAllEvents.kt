package garden.bots.scarlet.backend

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.languages.compileFunction

//TODO return a Result
fun loadAllEventsAndCompile(events: MutableMap<String, Function>) {
  getAllEvents().let { result ->
    when {
      result.isFailure -> {
        println(result.exceptionOrNull()?.message)
      }
      result.isSuccess -> {
        result.getOrNull()?.forEach { key, value ->
          events[key] = value
          val currentEvent: Function = value

          compileFunction(currentEvent.code, currentEvent.language)
            .onFailure { /* === 😡 Failure === */
              println(result.exceptionOrNull()?.message)
            }
            .onSuccess { /* === 🙂 Success === */
              println("event ${currentEvent.name} [${currentEvent.language}] is compiled")
            }

        }
      }
      else -> {
        TODO()
      }
    }
  }
}
