package di

import org.koin.dsl.module
import presentation.screen.chat.ChatScreenModel

val viewModelModule = module {
    factory { ChatScreenModel(get()) }
}