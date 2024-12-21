package com.example.weatherapp.fragments.location

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.RemoteLocation
import com.example.weatherapp.databinding.FragmentLocationBinding
import com.example.weatherapp.fragments.home.HomeFragment
import com.example.weatherapp.fragments.manager_location.ManagerLocationViewModel
import com.example.weatherapp.network.repositoty.WeatherDataRepository
import org.koin.android.ext.android.inject

class LocationFragment : Fragment() {
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = requireNotNull(_binding)

    // Inject WeatherDataRepository bằng Koin
    private val weatherDataRepository: WeatherDataRepository by inject()

    // Khởi tạo ManagerLocationViewModel từ Activity để chia sẻ với các Fragment khác
    private val managerLocationViewModel: ManagerLocationViewModel by lazy {
        ViewModelProvider(requireActivity())[ManagerLocationViewModel::class.java]
    }

    // Khởi tạo LocationViewModel bằng cách sử dụng factory
    private val locationViewModel: LocationViewModel by lazy {
        ViewModelProvider(
            this,
            LocationViewModelFactory(weatherDataRepository, managerLocationViewModel)
        )[LocationViewModel::class.java]
    }

    private val locationsAdapter = LocationsAdapter(
        onLocationClicked = { remoteLocation ->
            setLocation(remoteLocation)
        },
        onAddLocationClicked = { remoteLocation ->
            // Gọi phương thức getLocationWeather để lấy thông tin thời tiết
            locationViewModel.getLocationWeather(remoteLocation)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setupLocationsRecyclerView()
        setObservers()
    }

    private fun setupLocationsRecyclerView() {
        with(binding.locationRecyclerView) {
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            adapter = locationsAdapter
        }
    }

    private fun setListeners() {
        binding.imageClose.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.inputSearch.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard()
                val query = binding.inputSearch.editText?.text
                if (query.isNullOrBlank()) return@setOnEditorActionListener true
                searchLocation(query.toString())
            }
            return@setOnEditorActionListener true
        }
    }

    private fun setLocation(remoteLocation: RemoteLocation) {
        with(remoteLocation) {
            val locationText = "$name, $region, $country"
            setFragmentResult(
                requestKey = HomeFragment.REQUEST_KEY_MANUAL_LOCATION_SEARCH,
                result = bundleOf(
                    HomeFragment.KEY_LOCATION_TEXT to locationText,
                    HomeFragment.KEY_LATITUDE to lat,
                    HomeFragment.KEY_LONGITUDE to lon
                )
            )
            findNavController().popBackStack()
        }
    }

    private fun setObservers() {
        locationViewModel.searchResult.observe(viewLifecycleOwner) {
            val searchResultDataState = it ?: return@observe
            if (searchResultDataState.isLoading) {
                binding.locationRecyclerView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
            searchResultDataState.locations?.let { remoteLocations ->
                binding.locationRecyclerView.visibility = View.VISIBLE
                locationsAdapter.setData(remoteLocations)
            }
            searchResultDataState.error?.let { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchLocation(query: String) {
        locationViewModel.searchLocation(query)
    }

    private fun hideSoftKeyboard() {
        val inputManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            binding.inputSearch.windowToken, 0
        )
    }
}
