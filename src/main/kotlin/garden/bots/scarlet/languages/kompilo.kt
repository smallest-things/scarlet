package garden.bots.scarlet.languages

import org.graalvm.polyglot.*
import javax.script.Invocable
import javax.script.ScriptEngineManager
import kotlin.Result

val scriptContext: Context = Context.newBuilder().allowAllAccess(true).build()

/* Kotlin support */
val engine = ScriptEngineManager().getEngineByExtension("kts")!!
val invoker = engine as Invocable
/* Kotlin support */

fun compileKotlinFunction(functionCode: String) : Result<Any> {
  return Result.runCatching {
    engine.eval(functionCode)
  }
}

fun compileGraalVMFunction(functionCode : String, language: String) : Result<Any> {
  return Result.runCatching {
    scriptContext.eval(Source.create(language, functionCode))
  }
}

fun compileFunction(functionCode : String, language: String) : Result<Any> {
  return when (language) {
    "kotlin" -> {
      compileKotlinFunction(functionCode)
    }
    else -> {
      compileGraalVMFunction(functionCode, language)
    }
  }
}

fun invokeKotlinFunction(name: String?, params: Any) : Result<Any> {
  return Result.runCatching {
    invoker.invokeFunction(name, params)
  }
}

fun invokeGraalVMFunction(name: String?, params: Any, language: String?) : Result<Any> {
  return Result.runCatching {
    scriptContext.getBindings(language).getMember(name).execute(params)
  }
}

fun invokeFunction(name: String?, params: Any, language: String?) : Result<Any> {
  return when (language) {
    "kotlin" -> {
      invokeKotlinFunction(name, params)
    }
    else -> {
      invokeGraalVMFunction(name, params, language)
    }
  }
}
