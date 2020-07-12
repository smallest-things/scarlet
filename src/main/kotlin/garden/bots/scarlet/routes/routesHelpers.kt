package garden.bots.scarlet.routes

import io.vertx.ext.web.RoutingContext

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
