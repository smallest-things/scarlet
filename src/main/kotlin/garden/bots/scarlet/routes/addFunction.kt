package garden.bots.scarlet.routes

import garden.bots.scarlet.languages.compileFunction
import garden.bots.scarlet.data.Function
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

fun createAddFunctionRoute(router: Router, functions: MutableMap<String, Function>, adminToken: String) {

  // add a function
  router.post("/functions").handler { context ->

    checkAdminToken(adminToken, context).let { tokenCheck ->
      when {
        /* === 😡 Failure === */
        tokenCheck.isFailure -> {
          context.response().putHeader("content-type", "application/json;charset=UTF-8")
            .end(
              json {
                obj("error" to "😡 bad token")
              }.encodePrettily()
            )
        }
        /* === 🙂 Success === */
        tokenCheck.isSuccess -> {
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

          val currentFunction: Function = Function(
            functionName,
            functionLanguage,
            functionCode,
            functionVersion
          )

          compileFunction(functionCode, functionLanguage).let { result ->
            when {
              /* === 😡 Failure === */
              result.isFailure -> { // compilation error
                context.response().putHeader("content-type", "application/json;charset=UTF-8")
                  .end(
                    json {
                      obj("error" to result.exceptionOrNull()?.message)
                    }.encodePrettily()
                  )
              }
              /* === 🙂 Success === */
              result.isSuccess -> { // compilation is OK
                //functions.put("$functionName:$functionVersion", currentFunction)
                functions.put(functionName, currentFunction)

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
      }
    }

  }
}
