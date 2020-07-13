package garden.bots.scarlet.backend

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.servicediscovery.Record
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend

class LocalStorageBackendService : ServiceDiscoveryBackend {
  override fun update(p0: Record?, p1: Handler<AsyncResult<Void>>?) {
    TODO("Not yet implemented")
  }

  override fun getRecords(p0: Handler<AsyncResult<MutableList<Record>>>?) {
    TODO("Not yet implemented")
  }

  override fun remove(p0: Record?, p1: Handler<AsyncResult<Record>>?) {
    TODO("Not yet implemented")
  }

  override fun remove(p0: String?, p1: Handler<AsyncResult<Record>>?) {
    TODO("Not yet implemented")
  }

  override fun init(p0: Vertx?, p1: JsonObject?) {
    TODO("Not yet implemented")
  }

  override fun store(p0: Record?, p1: Handler<AsyncResult<Record>>?) {
    TODO("Not yet implemented")
  }

  override fun getRecord(p0: String?, p1: Handler<AsyncResult<Record>>?) {
    TODO("Not yet implemented")
  }

}
