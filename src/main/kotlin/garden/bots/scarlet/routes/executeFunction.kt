package garden.bots.scarlet.routes

import garden.bots.scarlet.data.Function
import garden.bots.scarlet.languages.invokeFunction
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

fun createExecuteFunctionRoute(router: Router, functions: MutableMap<String, Function>, adminToken: String) {
  // execute function
  // add version ? How to deal with version (create an unique ID)
  router.post("/execute/:function_name").handler { context ->

    checkAdminToken(adminToken, context)
      .onFailure { /* === 😡 Failure === */
        context.response()
          .putHeader("content-type", "application/json;charset=UTF-8")
          .end(json { obj("error" to "😡 bad token") }.encodePrettily())
      }.onSuccess { /* === 🙂 Success === */
        val params = context.bodyAsJson
        //TODO: check structure of params
        // call the function
        val functionName=context.request().getParam("function_name")

        invokeFunction(
          functionName,
          params,
          functions[functionName]?.language
        ).onFailure { /* === 😡 Failure === */ // execution error
          context.response().putHeader("content-type", "application/json;charset=UTF-8")
            .end(
              json {
                obj("error" to it.message)
              }.encodePrettily()
            )
        }.onSuccess { /* === 🙂 Success === */ // execution is OK
          context.response().putHeader("content-type", "application/json;charset=UTF-8")
            .end(it.toString())
        }
      }
  }
}
