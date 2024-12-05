package com.example.weatherapp.dependency_injection

import com.example.weatherapp.network.repositoty.WeatherDataRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { WeatherDataRepository(weatherAPI = get()) }

}