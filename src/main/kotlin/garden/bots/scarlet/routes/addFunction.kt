package garden.bots.scarlet.routes

import garden.bots.scarlet.backend.saveFunction
import garden.bots.scarlet.languages.compileFunction
import garden.bots.scarlet.data.Function
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

fun createAddFunctionRoute(router: Router, functions: MutableMap<String, Function>, adminToken: String) {

  // add a function
  router.post("/functions").handler { context ->
    //println(context.bodyAsJson)
    checkAdminToken(adminToken, context)
      .onFailure { throwable ->
        context.response().putHeader("content-type", "application/json;charset=UTF-8")
          .end(json { obj("error" to throwable.message) }.encodePrettily())
      }
      .onSuccess {
        println("😀 success")
        val defaultFunctionLanguage = "js"
        val defaultFunctionName = "hello"

        val defaultFunctionCode = """
            function hello(params) {
              return {
                message: "👋 Hello World 🌍",
              }
            }
          """.trimIndent()

        val params = context.bodyAsJson
        //TODO: check structure of params
        val functionName = params.getString("name") ?: defaultFunctionName
        val functionLanguage = params.getString("language") ?:defaultFunctionLanguage
        val functionCode = params.getString("code") ?: defaultFunctionCode
        val functionVersion = params.getString("version") ?: "0.0.0"

        val currentFunction = Function(
          functionName,
          functionLanguage,
          functionCode,
          functionVersion
        )

        println(functionLanguage)
        println(functionCode)

        compileFunction(functionCode, functionLanguage)
          .onFailure { throwable ->
            println("😡 compilation !!!")
            /* === 😡 Failure === */
            context.response().putHeader("content-type", "application/json;charset=UTF-8")
              .end(
                json {
                  obj("error" to throwable.message)
                }.encodePrettily()
              )
          }
          .onSuccess {
            /* === 🙂 Success === */
            functions[functionName] = currentFunction
            saveFunction(currentFunction)

            context.response().putHeader("content-type", "application/json;charset=UTF-8")
              .end(
                json {
                  obj("result" to "function $functionName [$functionLanguage] is compiled")
                }.encodePrettily()
              )
          }
      }
  }
}
