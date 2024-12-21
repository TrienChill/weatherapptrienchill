package com.example.weatherapp.fragments.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    fun getPrivacyPolicyUrl(): String {
        return "https://forms.gle/unw7yRoGKtq2znS87"
    }
}
