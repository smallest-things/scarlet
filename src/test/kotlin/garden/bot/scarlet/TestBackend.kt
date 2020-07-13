package garden.bot.scarlet

import io.vertx.core.Vertx
import io.vertx.kotlin.core.json.get
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.servicediscovery.ServiceDiscovery
import io.vertx.servicediscovery.ServiceDiscoveryOptions




import org.junit.jupiter.api.Test

class TestBackend {
  @Test
  fun createServiceDiscovery() {
    println("👋 test: createServiceDiscovery")
    // === Discovery settings ===
    val storagePath: String = System.getenv("STORAGE_PATH") ?: "./"
    val serviceDiscoveryOptions = ServiceDiscoveryOptions()
    val discovery: ServiceDiscovery = ServiceDiscovery.create(
      Vertx.vertx(),
      serviceDiscoveryOptions.setBackendConfiguration(
        json { obj("storagePath" to storagePath ) }
      )
    )

    assert(discovery.options().backendConfiguration.get<String>("storagePath")=="./")
  }
}
