package com.example.weatherapp.fragments.manager_location

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.RemoteLocation
import com.example.weatherapp.databinding.FragmentManagerLocationBinding
import com.example.weatherapp.fragments.home.HomeFragment
import com.example.weatherapp.fragments.location.LocationViewModel
import com.example.weatherapp.fragments.location.LocationViewModelFactory
import com.example.weatherapp.network.repositoty.WeatherDataRepository
import org.koin.android.ext.android.inject

class ManagerLocationFragment : Fragment(R.layout.fragment_manager_location) {

    private var _binding: FragmentManagerLocationBinding? = null
    private val binding get() = _binding!!

    // Inject WeatherDataRepository bằng Koin
    private val weatherDataRepository: WeatherDataRepository by inject()

    // Khởi tạo ManagerLocationViewModel từ Activity để chia sẻ giữa các Fragment
    private val managerLocationViewModel: ManagerLocationViewModel by lazy {
        ViewModelProvider(requireActivity())[ManagerLocationViewModel::class.java]
    }

    // Khởi tạo LocationViewModel bằng Factory
    private val locationViewModel: LocationViewModel by lazy {
        ViewModelProvider(
            this,
            LocationViewModelFactory(weatherDataRepository, managerLocationViewModel)
        )[LocationViewModel::class.java]
    }

    private val locationAdapter = ManagerLocationsAdapter(
        onAddLocationClicked = { location ->
            locationViewModel.getLocationWeather(location)
        },
        onLongPress = { location ->
            showFavoriteDialog(location)
        },
        onLocationClicked = { location ->
            // Truyền dữ liệu vị trí về HomeFragment
            val locationText = "${location.name}, ${location.region}, ${location.country}"
            setFragmentResult(
                HomeFragment.REQUEST_KEY_MANUAL_LOCATION_SEARCH,
                bundleOf(
                    HomeFragment.KEY_LOCATION_TEXT to locationText,
                    HomeFragment.KEY_LATITUDE to location.lat,
                    HomeFragment.KEY_LONGITUDE to location.lon
                )
            )
            findNavController().popBackStack() // Quay về HomeFragment
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentManagerLocationBinding.bind(view)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewLocations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = locationAdapter

            // Thêm ItemTouchHelper để xử lý hành động kéo và xóa
            val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val location = locationAdapter.getItemAtPosition(position)
                        managerLocationViewModel.removeLocation(location)
                        Log.d("ManagerLocationFragment", "Location removed: ${location.name}")
                    }
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(this)

            // Thêm một view trống nếu không có dữ liệu
            val emptyView = TextView(context).apply {
                text = "Không có địa điểm nào"
                textSize = 16f
                gravity = Gravity.CENTER
            }
            (adapter as? ManagerLocationsAdapter)?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    checkIfEmpty()
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    checkIfEmpty()
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    checkIfEmpty()
                }

                private fun checkIfEmpty() {
                    val isEmpty = locationAdapter.itemCount == 0
                    emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
                    binding.recyclerViewLocations.visibility = if (isEmpty) View.GONE else View.VISIBLE
                }
            })
        }
    }


    // Thêm logic trong onViewCreated để xử lý sự kiện nhấn vào vị trí
    private fun observeViewModel() {
        managerLocationViewModel.locations.observe(viewLifecycleOwner) { locations ->
            Log.d("ManagerLocationFragment", "Observed locations: ${locations.size}")
            locationAdapter.updateLocations(locations)
        }
    }

    private fun showFavoriteDialog(location: RemoteLocation) {
        // Lưu trạng thái gốc của location
        val originalLocation = location.copy()

        AlertDialog.Builder(requireContext())
            .setTitle("Lưu vị trí")
            .setMessage("Bạn có muốn lưu vị trí này vào mục yêu thích?")
            .setPositiveButton("Có") { _, _ ->
                managerLocationViewModel.toggleFavorite(location)
            }
            .setNegativeButton("Không") { _, _ ->
                // Khôi phục trạng thái gốc của location nếu nhấn "Không"
                managerLocationViewModel.updateLocation(originalLocation)
            }
            .show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
