package garden.bots.scarlet

import garden.bots.scarlet.data.Function
import io.vertx.ext.web.LanguageHeader
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.jsonArrayOf
import io.vertx.kotlin.core.json.obj

fun checkAdminToken(adminToken: String, context: RoutingContext): Result<String> {

  val check: Boolean = adminToken.isBlank() || context.request().getHeader("SCARLET_ADMIN_TOKEN") == adminToken

  return when(check) {
    false -> {
      Result.failure(Exception("😡 bad admin token"))
    }
    true -> {
      Result.success(adminToken)
    }
  }
}

fun createRoutes(router: Router, functions: MutableMap<String, Function>, adminToken: String) {
  router.route().handler(BodyHandler.create())

  router.route("/*").handler(StaticHandler.create().setCachingEnabled(false))

  // add a function
  router.post("/functions").handler { context ->

    checkAdminToken(adminToken, context).let { tockenCheck ->
      when {
        /* === 😡 Failure === */
        tockenCheck.isFailure -> {
          context.response().putHeader("content-type", "application/json;charset=UTF-8")
            .end(
              json {
                obj("error" to "😡 bad token")
              }.encodePrettily()
            )
        }
        /* === 🙂 Success === */
        tockenCheck.isSuccess -> {
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
                  .end(result.getOrNull().toString())
              }
            }
          }
        }
      }
    }

  }

  // execute function
  // add version ? How to deal with version (create an unique ID)
  router.post("/execute/:function_name").handler { context ->

    checkAdminToken(adminToken, context).let {tockenCheck ->
      when {
        /* === 😡 Failure === */
        tockenCheck.isFailure -> {
          context.response().putHeader("content-type", "application/json;charset=UTF-8")
            .end(
              json {
                obj("error" to "😡 bad token")
              }.encodePrettily()
            )
        }
        /* === 🙂 Success === */
        tockenCheck.isSuccess -> {
          val params = context.bodyAsJson
          //TODO: check structure of params
          // call the function
          val functionName=context.request().getParam("function_name")
          invokeFunction(
            functionName,
            params,
            functions.get(functionName)?.language
          ).let { result ->
            when {
              /* === 😡 Failure === */
              result.isFailure -> { // execution error
                context.response().putHeader("content-type", "application/json;charset=UTF-8")
                  .end(
                    json {
                      obj("error" to result.exceptionOrNull()?.message)
                    }.encodePrettily()
                  )
              }
              /* === 🙂 Success === */
              result.isSuccess -> { // execution is OK
                context.response().putHeader("content-type", "application/json;charset=UTF-8")
                  .end(result.getOrNull().toString())
              }
            }
          }
        } // end of isSuccess
      } // end of when
    } // end of let

  }

  router.get("/functions").handler { context ->

    checkAdminToken(adminToken, context).let { tockenCheck ->
      when {
        /* === 😡 Failure === */
        tockenCheck.isFailure -> {
          context.response().putHeader("content-type", "application/json;charset=UTF-8")
            .end(
              json {
                obj("error" to "😡 bad token")
              }.encodePrettily()
            )
        }
        /* === 🙂 Success === */
        tockenCheck.isSuccess -> {
          context.response().putHeader("content-type", "application/json;charset=UTF-8")
            .end(jsonArrayOf(functions).encodePrettily())
        }
      }
    }
  }

  // TODO add health check etc ...

}
