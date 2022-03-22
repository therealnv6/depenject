package io.github.devrawr.inject.pipeline

import io.github.devrawr.inject.InjectionFinder
import io.github.devrawr.inject.binding.InjectionBinding

class PipelinedSearch(private val finder: InjectionFinder)
{
    val types = hashMapOf<Class<*>, String>()

    inline fun <reified T : Any> addType(name: String = "*"): PipelinedSearch
    {
        return this.also {
            it.types[T::class.java] = name
        }
    }

    /**
     * Get the result of the calls above.
     *
     * All of the [types] will be merged into a [PipelinedResult]
     * by looping through the [InjectionFinder.binders]'s binders once,
     * and mapping them to one and other.
     *
     * @return the [PipelinedResult] instance
     */
    fun search(): PipelinedResult
    {
        val binders = finder.binders
        val results = hashMapOf<Class<*>, InjectionBinding<*>>()

        // single loop... otherwise we'd basically defeat the purpose of pipelining, obviously.
        for (binder in binders)
        {
            val id = this.types[binder.key]

            if (id != null)
            {
                if (id == "*")
                {
                    results[binder.key] = binder.value.first()
                } else
                {
                    for (binding in binder.value)
                    {
                        if (id == binding.name)
                        {
                            results[binder.key] = binding
                            break
                        }
                    }
                }
            }
        }

        return PipelinedResult(results)
    }
}