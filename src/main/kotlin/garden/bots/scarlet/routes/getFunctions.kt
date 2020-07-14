package garden.bots.scarlet.routes

import garden.bots.scarlet.data.Function
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj


fun createGetFunctionsRoute(router: Router, functions: MutableMap<String, Function>, adminToken: String) {

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
          functions.toList()
          context.response().putHeader("content-type", "application/json;charset=UTF-8")
            .end(
              json { obj("functions" to functions.map { entry -> entry.value })}.encodePrettily()
            )
        }
      }
    }
  }

  // TODO add health check etc ...

}
