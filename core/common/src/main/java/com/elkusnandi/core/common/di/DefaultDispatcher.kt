package com.elkusnandi.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val defaultDispatcher: DefaultDispatcher)

enum class DefaultDispatcher {
    Default, IO
}