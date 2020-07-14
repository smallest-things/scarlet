package garden.bots.scarlet.events

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.languages.invokeFunction

// execute only if exists
fun triggerEvent(eventName: String, params: Any, events: MutableMap<String, Function>) : Result<Any?>{

  when {
    events[eventName] != null -> {
      invokeFunction(
        eventName,
        params,
        events.get(eventName)?.language
      ).let { result ->
        return when {
          /* === 😡 Failure === */
          result.isFailure -> { // execution error
            Result.failure(Exception(result.exceptionOrNull()?.message))
          }
          /* === 🙂 Success === */
          result.isSuccess -> { // execution is OK
            Result.success(result.getOrNull())
          }
          else -> {
            Result.failure(Exception("🤔 unknown failure when initializing"))
          }
        }
      }
    }
    else -> { TODO() }
  }






}
