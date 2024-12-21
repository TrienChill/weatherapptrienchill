package com.example.weatherapp.fragments.manager_location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.RemoteLocation
import com.example.weatherapp.databinding.ItemContainerLocationBinding

class ManagerLocationsAdapter(
    private val onAddLocationClicked: (RemoteLocation) -> Unit,
    private val onLongPress: (RemoteLocation) -> Unit,
    private val onLocationClicked: (RemoteLocation) -> Unit // Thêm sự kiện mới
) : RecyclerView.Adapter<ManagerLocationsAdapter.ViewHolder>() {

    private var locations: List<RemoteLocation> = listOf()

    fun updateLocations(newLocations: List<RemoteLocation>) {
        val oldLocations = locations
        locations = newLocations.sortedByDescending { it.isFavorite }

        // So sánh các danh sách cũ và mới để xác định những thay đổi cần thiết
        val diff = DiffUtil.calculateDiff(LocationDiffCallback(oldLocations, locations))
        diff.dispatchUpdatesTo(this)
    }

    fun getItemAtPosition(position: Int): RemoteLocation {
        return locations[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContainerLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        // Truyền callback vào ViewHolder
        return ViewHolder(binding, onAddLocationClicked, onLongPress, onLocationClicked)
    }

    override fun getItemCount(): Int = locations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locations[position]
        holder.bind(location)
    }

    inner class ViewHolder(
        private val binding: ItemContainerLocationBinding,
        private val onAddLocationClicked: (RemoteLocation) -> Unit,
        private val onLongPress: (RemoteLocation) -> Unit,
        private val onLocationClicked: (RemoteLocation) -> Unit // Thêm đối số mới
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(location: RemoteLocation) {
            // Hiển thị địa điểm đầy đủ
            val locationText = "${location.name}, ${location.region}, ${location.country}"
            binding.textRemoteLocation.text = locationText

            // Hiển thị icon favorite
            binding.imageFavorite.visibility = if (location.isFavorite) View.VISIBLE else View.GONE

            // Xử lý sự kiện long press
            binding.root.setOnLongClickListener {
                onLongPress(location)
                true
            }

            // Xử lý sự kiện thêm địa điểm
            binding.btnAddLocation.setOnClickListener {
                onAddLocationClicked(location)
            }

            // Thêm sự kiện click vào item
            binding.root.setOnClickListener {
                onLocationClicked(location) // Gọi sự kiện khi nhấn vào vị trí
            }
        }
    }
}
