package com.example.weatherapp.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherInfoBinding

class WeatherInfoFragment : Fragment() {
    private var _binding: FragmentWeatherInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lấy thông tin ứng dụng
        val appName = getString(R.string.app_name)
        val appVersion = getAppVersion()
        val latestVersion = "1.0"  // Cập nhật bằng cách lấy phiên bản mới nhất từ một API nếu cần

        // Cập nhật UI
        binding.apply {
            appNameTextView.text = appName
            appVersionTextView.text = getString(R.string.app_version, appVersion)
            latestVersionTextView.text = getString(R.string.latest_version, latestVersion)
        }
    }

    private fun getAppVersion(): String {
        return try {
            val packageInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            packageInfo.versionName ?: "Unknown" // Nếu versionName là null, trả về "Unknown"
        } catch (e: Exception) {
            "Unknown" // Nếu có lỗi xảy ra, trả về "Unknown"
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
