package garden.bot.scarlet

import garden.bots.scarlet.backend.getAllFunctions
import garden.bots.scarlet.backend.initializeStorage
import garden.bots.scarlet.backend.saveFunction
import garden.bots.scarlet.data.Function
import org.junit.jupiter.api.Test

class TestBackend {

  @Test
  fun step_01_initializeLocalStorage() {

    initializeStorage().let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          assert(true)
        }
      }
    }

  }

  @Test
  fun step_02_addFunction() {
    println("👋 test: addFunction")

    val currentFunction: Function = Function(
      "greetings",
      "python",
      "# foo",
      "6.6.6"
    )

    saveFunction(currentFunction).let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          println("step_02_addFunction: ${result}")
          assert(true)
        }
      }
    }

  }

  @Test
  fun step_03_addFunction() {
    println("👋 test: addFunction")

    val defaultFunctionCode = """
      function wow(params) {
        return {
          message: "👋 Hello World 🌍",
          author: "John Doe"
        }
      }
    """.trimIndent()

    val currentFunction: Function = Function(
      "wow",
      "js",
      defaultFunctionCode,
      "6.6.6"
    )

    saveFunction(currentFunction).let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          println("step_03_addFunction: ${result}")
          assert(true)
        }
      }
    }

  }


  @Test
  fun step_04_getAllfunctions() {
    println("👋 test: getAll")

    getAllFunctions().let { result ->
      when {
        result.isFailure -> {
          assert(false)
        }
        result.isSuccess -> {
          println("step_04_getAllfunctions: ${result}")
          assert(true)
        }
      }
    }
  }

}
