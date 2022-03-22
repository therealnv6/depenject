import io.github.devrawr.inject.Inject
import io.github.devrawr.inject.Injector
import io.github.devrawr.inject.inject
import org.junit.jupiter.api.Test

val injector = Injector.create<String>()

class InjectTest
{
    @Test
    fun inject()
    {
        injector.bind<String>() to "hey" named "lol"
        injector.bind<Int>() to 5
        injector.bind<Int>() to 9 named "number2"

        println(
            """
            string: ${InjectionTest.string}
            number: ${InjectionTest.number}
            number2: ${InjectionTest.number2}
        """.trimIndent()
        )
    }
}

object InjectionTest
{
    // simple individual search, from global `injector` field
    val number by injector.inject<Int>()

    // pipelined search
    private val pipelined = Inject.by<String>().pipelined()
        .addType<String>()
        .addType<Int>("number2") // injection is named "number2"
        .search()

    val string by pipelined.inject<String>()
    val number2 by pipelined.inject<Int>()
}