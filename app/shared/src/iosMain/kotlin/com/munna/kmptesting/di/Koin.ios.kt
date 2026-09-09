package com.munna.kmptesting.di

import com.munna.kmptesting.createSessionManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { createSessionManager() }
}
