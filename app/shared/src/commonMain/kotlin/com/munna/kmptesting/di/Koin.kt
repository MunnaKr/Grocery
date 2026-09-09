package com.munna.kmptesting.di

import com.munna.kmptesting.*
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.compose.viewmodel.dsl.viewModelOf

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(commonModule, platformModule)
    }

// for iOS
fun initKoin() = initKoin {}

expect val platformModule: Module

val commonModule = module {
    single { ApiClient() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    viewModelOf(::AuthViewModel)
}
