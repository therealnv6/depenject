# depenject

`depenject` is a lightweight, minimalistic dependency injection library for Kotlin/JVM.

* Our goal is similar to [flavor's](https://github.com/GrowlyX/flavor) to simplify the usage of traditional dependency
  injectors, but instead an approach without annotations.
* Just like [flavor](https://github.com/GrowlyX/flavor), we use kotlin-exclusive features such
  as [reified types & inline functions](https://kotlinlang.org/docs/inline-functions.html)
  and [ReadWriteProperty](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-read-write-property/)
    * [reified types & inline functions](https://kotlinlang.org/docs/inline-functions.html) allows us to offer an easier
      approach to injecting & binding.
    * [ReadWriteProperty](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-read-write-property/) is what
      we use to avoid having to use annotations. Soon will be used for more than what it's currently doing.
        * This also makes it (unfortunately) incompatible with Java and other JVM-specific languages.

# Features

* Basic object binding to key, with an optional `named` property to identify key/objects.
* Basic injection using the
  aforementioned [ReadWriteProperty](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-read-write-property/)
  .
* Pipelining to easily retrieve several objects in just a single loop, allowing for further startup optimization.

# Should this be used

If you prefer to not use annotations and are not planning to use this for production, feel free to test it out and/or
use it for your own projects. Other than that, it's just a simple library I made for fun. Not expecting people to
actually use it.

# Usage

After reading all of this and you still feel like you want to use this, here's some basic usages for injecting &
binding.

```kotlin
val injector =
    Injector.create<String>() // this injector will now be bound to the String parent, and will thus be able to be retrieved using the String parent identifier.

class InjectTest
{
    @Test
    fun inject()
    {
        injector.bind<String>() to "hey" named "lol"
        injector.bind<Int>() to 5
        injector.bind<Int>() to 9 named "number2" // we're binding the Int type with value 9 to an identifier, namely the "number2" identifier.

        assertEquals("hey", InjectionTest.string)
        assertEquals(5, InjectionTest.number)
        assertEquals(9, InjectionTest.number2)
    }
}

object InjectionTest
{
    // simple individual search, from global `injector` field
    val number by injector.inject<Int>()

    // pipelined search
    private val pipelined =
        Inject.by<String>().pipelined() // here we're retrieving the registered Injector to the "String" parent.
            .addType<String>()
            .addType<Int>("number2") // injection is named "number2"
            .search()


    // all methods following the .pipelined().search() method are the exact same implementation as in Injector and Inject, 
    // but using a pipelined implementation for the backend instead, which means the methods are exactly the same as the aftermentioned classes.
    val string by pipelined.inject<String>()
    val number2 by pipelined.inject<Int>()
}
```