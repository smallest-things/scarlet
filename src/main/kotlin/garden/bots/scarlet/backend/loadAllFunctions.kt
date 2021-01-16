package garden.bots.scarlet.backend

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.languages.compileFunction

//TODO return a Result 🤔
fun loadAllFunctionsAndCompile(functions: MutableMap<String, Function>) {

  getAllFunctions()
    .onFailure { throwable ->
      println(throwable.message)
    }
    .onSuccess { mutableMap ->
      mutableMap.forEach { (key, value) ->
        functions[key] = value
        val currentFunction: Function = value

        compileFunction(currentFunction.code)
          .onFailure { throwable ->
            /* === 😡 Failure === */
            println(throwable.message)
          }
          .onSuccess {
            /* === 🙂 Success === */
            println("function ${currentFunction.name} is compiled")
          }

      }
    }

}
