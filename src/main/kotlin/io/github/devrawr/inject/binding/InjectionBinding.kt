package io.github.devrawr.inject.binding

import kotlin.properties.Delegates

class InjectionBinding<T : Any>
{
    var instance by Delegates.notNull<T>()
    var name = "*"

    infix fun named(name: String): InjectionBinding<T>
    {
        return this.also {
            it.name = name
        }
    }

    infix fun to(value: T): InjectionBinding<T>
    {
        return this.also {
            it.instance = value
        }
    }
}