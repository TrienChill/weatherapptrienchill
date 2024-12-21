package com.example.weatherapp.fragments.location

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.RemoteLocation
import com.example.weatherapp.databinding.ItemContainerLocationBinding

class LocationsAdapter(
    private val onLocationClicked: (RemoteLocation) -> Unit,
    private val onAddLocationClicked: (RemoteLocation) -> Unit // Thêm callback cho nút thêm vị trí

) : RecyclerView.Adapter<LocationsAdapter.LocationsViewHolder>() {
    private val locations = mutableListOf<RemoteLocation>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<RemoteLocation>) {
        locations.clear()
        locations.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        return LocationsViewHolder(
            ItemContainerLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onLocationClicked, onAddLocationClicked // Truyền callback mới vào

        )
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        holder.bind(remoteLocation = locations[position])
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    inner class LocationsViewHolder(
        private val binding: ItemContainerLocationBinding,
        private val onLocationClicked: (RemoteLocation) -> Unit,
        private val onAddLocationClicked: (RemoteLocation) -> Unit // Thêm callback cho nút thêm vị trí
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(remoteLocation: RemoteLocation) {
            with(remoteLocation) {
                // Sử dụng context để lấy chuỗi tài nguyên với các tham số
                val locationText = binding.root.context.getString(
                    R.string.location_display_format,
                    name,
                    region,
                    country
                )
                binding.textRemoteLocation.text = locationText

                binding.root.setOnClickListener { onLocationClicked(remoteLocation) }

                // Xử lý sự kiện click vào nút thêm vị trí
                binding.btnAddLocation.setOnClickListener {
                    onAddLocationClicked(remoteLocation)
                }
            }
        }
    }
}
