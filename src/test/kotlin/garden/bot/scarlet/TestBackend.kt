package garden.bot.scarlet

import garden.bots.scarlet.backend.initializeStorage
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
}
