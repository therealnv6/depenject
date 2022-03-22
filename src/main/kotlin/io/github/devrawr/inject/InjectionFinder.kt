package io.github.devrawr.inject

import io.github.devrawr.inject.binding.InjectionBinding
import io.github.devrawr.inject.pipeline.PipelinedSearch
import kotlin.properties.ReadWriteProperty

abstract class InjectionFinder
{
    abstract val binders: Map<Class<*>, MutableList<InjectionBinding<*>>>

    open fun <T : Any> inject(type: Class<T>, name: String = "*"): ReadWriteProperty<Any?, T>
    {
        val binder = this
            .binders[type]
            ?.firstOrNull {
                name == "*" || it.name == name
            } ?: throw IllegalArgumentException("Nothing bound by ${type.name} type.")

        return InjectedReadWriteProperty(binder as InjectionBinding<T>)
    }

    open fun pipelined(): PipelinedSearch
    {
        return PipelinedSearch(this)
    }
}

inline fun <reified T : Any> InjectionFinder.inject(name: String = "*") = this.inject(T::class.java, name)