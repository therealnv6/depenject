package io.github.devrawr.inject

import io.github.devrawr.inject.binding.InjectionBinding
import io.github.devrawr.inject.pipeline.PipelinedSearch
import kotlin.properties.ReadWriteProperty

object Inject : InjectionFinder()
{
    override val binders: HashMap<Class<*>, MutableList<InjectionBinding<*>>>
        get()
        {
            return injectors.values
                .first()
                .binders
        }

    val injectors = hashMapOf<Class<*>, Injector>()

    inline fun <reified T : Any> by(): Injector
    {
        return injectors[T::class.java] as Injector
    }

    override fun <T : Any> inject(type: Class<T>, name: String): ReadWriteProperty<Any?, T>
    {
        return injectors.values.first().inject(type, name)
    }

    override fun pipelined(): PipelinedSearch
    {
        return injectors.values.first().pipelined()
    }
}