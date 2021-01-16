package garden.bots.scarlet.languages

import javax.script.Invocable
import javax.script.ScriptEngineManager
import kotlin.Result

val engine = ScriptEngineManager().getEngineByExtension("kts")!!
val invoker = engine as Invocable

fun compileFunction(functionCode : String) : Result<Any> {
  return Result.runCatching {
    engine.eval(functionCode)
  }
}

fun invokeFunction(name: String?, params: Any) : Result<Any> {
  return Result.runCatching {
    invoker.invokeFunction(name, params)
  }
}
