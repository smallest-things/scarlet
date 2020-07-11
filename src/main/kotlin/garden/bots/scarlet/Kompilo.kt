package garden.bots.scarlet

import org.graalvm.polyglot.*
import kotlin.Result

val scriptContext: Context = Context.newBuilder().allowAllAccess(true).build()

fun compileFunction(functionCode : String, language: String) : Result<Any> {
  return try {
    Result.success(scriptContext.eval(Source.create(language, functionCode)))
  } catch (exception: Exception) {
    Result.failure<Exception>(exception)
  }
}

fun invokeFunction(name: String?, params: Any, language: String?) : Result<Any> {
  return try {
    Result.success(scriptContext.getBindings(language).getMember(name).execute(params))
  } catch (exception: Exception) {
    Result.failure<Exception>(exception)
  }
}
