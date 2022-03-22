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
                for (binding in binder.value)
                {
                    if (id == "*" || id == binding.name)
                    {
                        results[binder.key] = binding
                        break
                    }
                }
            }
        }

        return PipelinedResult(results)
    }
}