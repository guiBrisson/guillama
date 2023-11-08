package data.di

import data.repository.OllamaRepositoryImpl
import domain.repository.OllamaRepository
import org.koin.dsl.module

val dataModule = module {
    single<OllamaRepository> { OllamaRepositoryImpl() }
}