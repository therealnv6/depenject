package io.github.devrawr.inject

import io.github.devrawr.inject.binding.InjectionBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Injector : InjectionFinder()
{
    override val binders = hashMapOf<Class<*>, MutableList<InjectionBinding<*>>>()

    companion object
    {
        inline fun <reified T : Any> create(): Injector
        {
            return Injector().also {
                Inject.injectors[T::class.java] = it
            }
        }
    }

    inline fun <reified T : Any> bind(): InjectionBinding<T>
    {
        return InjectionBinding<T>().also {
            this.binders
                .putIfAbsent(
                    T::class.java, mutableListOf()
                )

            this.binders[T::class.java]!! += it
        }
    }
}

class InjectedReadWriteProperty<T : Any>(private val binding: InjectionBinding<T>) : ReadWriteProperty<Any?, T>
{
    override fun getValue(thisRef: Any?, property: KProperty<*>): T
    {
        return this.binding.instance
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
    {
    }
}