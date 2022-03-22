package io.github.devrawr.inject.pipeline

import io.github.devrawr.inject.InjectedReadWriteProperty
import io.github.devrawr.inject.InjectionFinder
import io.github.devrawr.inject.binding.InjectionBinding
import kotlin.properties.ReadWriteProperty

class PipelinedResult(
    private val results: Map<Class<*>, InjectionBinding<*>>
) : InjectionFinder()
{
    // don't use, we're using the results field.
    override val binders = mapOf<Class<*>, MutableList<InjectionBinding<*>>>()

    override fun <T : Any> inject(type: Class<T>, name: String): ReadWriteProperty<Any?, T>
    {
        val binder = results[type]
            ?: throw IllegalArgumentException("Nothing bound by $type type.")

        return InjectedReadWriteProperty(binder as InjectionBinding<T>)
    }
}