package di

import org.koin.dsl.module
import presentation.screen.chat.ChatScreenModel
import presentation.screen.main.MainScreenModel

val viewModelModule = module {
    factory { ChatScreenModel(get()) }
    factory { MainScreenModel(get()) }
}
