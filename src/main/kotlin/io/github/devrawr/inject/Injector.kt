package io.github.devrawr.inject

import io.github.devrawr.inject.binding.InjectionBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Injector : InjectionFinder()
{
    override val binders = hashMapOf<Class<*>, MutableList<InjectionBinding<*>>>()

    companion object
    {
        /**
         * Create a new [Injector] instance.
         *
         * Uses the [T] type parameter has parent type
         * to register to. This parent type can be used to
         * later on identify the injector in the [Inject.injectors] list.
         *
         * @return the newly created [Injector]
         */
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

/**
 * Simple [ReadWriteProperty] implementation to use our own [InjectionBinding.instance] variable.
 */
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