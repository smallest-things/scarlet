package garden.bots.scarlet.routes

import garden.bots.scarlet.data.MqttSubscription
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj


fun createGetSubscriptionsRoute(router: Router, subscriptions: MutableMap<String,MqttSubscription>, adminToken: String) {

  router.get("/subscriptions").handler { context ->

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
          subscriptions.toList()
          context.response().putHeader("content-type", "application/json;charset=UTF-8")
            .end(
              json { obj("subscriptions" to subscriptions.map { entry -> entry.value })}.encodePrettily()
            )
        }
      }
    }
  }

  // TODO add health check etc ...

}
