package io.github.devrawr.inject

import io.github.devrawr.inject.binding.InjectionBinding
import io.github.devrawr.inject.pipeline.PipelinedSearch
import kotlin.properties.ReadWriteProperty

abstract class InjectionFinder
{
    abstract val binders: Map<Class<*>, MutableList<InjectionBinding<*>>>

    /**
     * Inject a variable as a certain binder.
     *
     * Finds a [InjectionBinding] by the provided [T] class type.
     *
     * Uses the provided [name] to (potentially) identify
     * the binder with. Returns any binder if the identifier is "*".
     *
     * @param type the type to find the [InjectionBinding] with
     * @param name the identifier used to identify binders
     * @return a [ReadWriteProperty], used to get the variable's value from.
     */
    open fun <T : Any> inject(type: Class<T>, name: String = "*"): ReadWriteProperty<Any?, T>
    {
        val binder = this
            .binders[type]
            ?.firstOrNull {
                name == "*" || it.name == name
            } ?: throw IllegalArgumentException("Nothing bound by ${type.name} type.")

        return InjectedReadWriteProperty(binder as InjectionBinding<T>)
    }

    /**
     * Get a [PipelinedSearch] object from the current [InjectionFinder]
     *
     * The pipelined search can be used for further optimizing
     * your program's startup (or other injection fetching) by
     * simply merging several calls into a several one, allowing us to
     * only have to loop through the binder list once.
     *
     * @return new instance of the [PipelinedSearch] object.
     */
    open fun pipelined(): PipelinedSearch
    {
        return PipelinedSearch(this)
    }
}

inline fun <reified T : Any> InjectionFinder.inject(name: String = "*") = this.inject(T::class.java, name)