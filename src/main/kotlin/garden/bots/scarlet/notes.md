About Result type:

- https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/
- https://stackoverflow.com/questions/52631827/why-cant-kotlin-result-be-used-as-a-return-type

You need to do this to use it:

If using maven:
```xml
<plugin>
    <artifactId>kotlin-maven-plugin</artifactId>
    <configuration>
        <jvmTarget>1.8</jvmTarget>
        <args>
            <arg>-Xallow-result-return-type</arg>
        </args>
    </configuration>
    <groupId>org.jetbrains.kotlin</groupId>
    <version>${kotlin.version}</version>
```

If using gradle:
```kotlin
compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = ["-Xallow-result-return-type"]


}
compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = ["-Xallow-result-return-type"]
}
```
