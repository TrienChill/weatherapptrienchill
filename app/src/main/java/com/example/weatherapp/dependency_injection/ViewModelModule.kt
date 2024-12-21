package com.example.weatherapp.dependency_injection

import com.example.weatherapp.fragments.home.HomeViewModel
import com.example.weatherapp.fragments.location.LocationViewModel
import com.example.weatherapp.fragments.location.LocationViewModelFactory
import com.example.weatherapp.fragments.manager_location.ManagerLocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(weatherDataRepository = get()) }
    viewModel { LocationViewModel(weatherDataRepository = get(), managerLocationViewModel = get()) }
    viewModel { ManagerLocationViewModel() }  // Thêm dòng này
    factory { LocationViewModelFactory(get(), get()) } // Đăng ký factory cho LocationViewModel

}
