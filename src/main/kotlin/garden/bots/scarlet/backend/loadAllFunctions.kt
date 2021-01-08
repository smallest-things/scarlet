package garden.bots.scarlet.backend

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.languages.compileFunction

//TODO return a Result
fun loadAllFunctionsAndCompile(functions: MutableMap<String, Function>) {
  getAllFunctions().let { result ->
    when {
      result.isFailure -> {
        println(result.exceptionOrNull()?.message)
      }
      result.isSuccess -> {
        //println("👀 all functions: ${result}")
        result.getOrNull()?.forEach { key, value ->

          functions[key] = value

          val currentFunction: Function = value

          compileFunction(currentFunction.code, currentFunction.language)
            .onFailure { /* === 😡 Failure === */
              println(result.exceptionOrNull()?.message)
            }
            .onSuccess { /* === 🙂 Success === */
              println("function ${currentFunction.name} [${currentFunction.language}] is compiled")
            }

        }

      }
      else -> {
        TODO()
      }
    }
  }
}
