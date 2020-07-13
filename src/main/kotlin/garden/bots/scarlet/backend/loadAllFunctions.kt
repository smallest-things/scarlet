package garden.bots.scarlet.backend

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.languages.compileFunction

fun loadAllFunctionsAndCompile(functions: MutableMap<String, Function>) {
  getAllFunctions().let { result ->
    when {
      result.isFailure -> {
        println(result.exceptionOrNull()?.message)
      }
      result.isSuccess -> {
        //println("👀 all functions: ${result}")
        result.getOrNull()?.forEach { key, value ->

          functions.put(key, value)

          val currentFunction: Function = value

          compileFunction(currentFunction.code, currentFunction.language).let { compilationResult ->
            when {
              /* === 😡 Failure === */
              compilationResult.isFailure -> { // compilation error
                println(result.exceptionOrNull()?.message)
              }
              /* === 🙂 Success === */
              compilationResult.isSuccess -> { // compilation is OK
                println("function ${currentFunction.name} [${currentFunction.language}] is compiled")
              }
            }
          }
        }

      }
      else -> {
        TODO()
      }
    }
  }
}
