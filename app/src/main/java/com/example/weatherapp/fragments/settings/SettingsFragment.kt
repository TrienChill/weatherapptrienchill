package com.example.weatherapp.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNotificationSettings()
        setupInformationSettings()
    }

    private fun setupNotificationSettings() {
        binding.notificationSettingsCard.setOnClickListener {
            Intent().also { intent ->
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                startActivity(intent)
            }
        }
    }

    private fun setupInformationSettings() {
        binding.apply {
            // Privacy Policy
            privacyPolicyCard.setOnClickListener {
                openUrl(viewModel.getPrivacyPolicyUrl())
            }

            // Permissions
            permissionsCard.setOnClickListener {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { intent ->
                    intent.data = Uri.fromParts("package", requireContext().packageName, null)
                    startActivity(intent)
                }
            }

            // Weather Information
            weatherInfoCard.setOnClickListener {
                // Navigate to weather information fragment
                // Implementation depends on your navigation setup
                // navigationController.navigate(R.id.action_settingsFragment_to_weatherInfoFragment)
                findNavController().navigate(R.id.action_settingsFragment_to_weatherInfoFragment)
            }

            // Contact Us
            contactUsCard.setOnClickListener {
                val email = "support@weatherapp.com"
                val subject = "Weather App Support"
                Intent(Intent.ACTION_SENDTO).also { intent ->
                    intent.data = Uri.parse("mailto:")
                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                    startActivity(Intent.createChooser(intent, "Send email"))
                }
            }
        }
    }

    private fun openUrl(url: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).also { intent ->
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}