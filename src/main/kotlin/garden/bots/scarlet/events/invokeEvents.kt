package garden.bots.scarlet.events

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.languages.invokeFunction

// execute only if exists
fun triggerEvent(eventName: String, params: Any, events: MutableMap<String, Function>) : Result<Any?> {

  when {
     events[eventName] != null -> {
      return invokeFunction(
        eventName,
        params,
        events[eventName]?.language
      ).onFailure { /* === 😡 Failure === */
        return Result.failure(it) // Exception(it.message)
      }.onSuccess { /* === 🙂 Success === */
        Result.success(it)
      }
    }
    else -> {
      //println("$eventName  is not implemented")
      return Result.failure(Exception("🖐 $eventName  is not implemented"))
    }
  }
}
