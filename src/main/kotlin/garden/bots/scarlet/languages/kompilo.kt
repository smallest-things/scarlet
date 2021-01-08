package garden.bots.scarlet.languages

import org.graalvm.polyglot.*
import kotlin.Result

val scriptContext: Context = Context.newBuilder().allowAllAccess(true).build()

fun compileFunction(functionCode : String, language: String) : Result<Any> {
  return Result.runCatching {
    scriptContext.eval(Source.create(language, functionCode))
  }
}

fun invokeFunction(name: String?, params: Any, language: String?) : Result<Any> {
  return Result.runCatching {
    scriptContext.getBindings(language).getMember(name).execute(params)
  }
}
