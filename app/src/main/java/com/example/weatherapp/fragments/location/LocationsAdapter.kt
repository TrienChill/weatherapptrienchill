package com.example.weatherapp.fragments.location

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.RemoteLocation
import com.example.weatherapp.databinding.ItemContainerLocationBinding

class LocationsAdapter(
    private val onLocationClicked: (RemoteLocation) -> Unit
) : RecyclerView.Adapter<LocationsAdapter.LocationsViewHolder>() {

    private val locations = mutableListOf<RemoteLocation>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<RemoteLocation>) {
        locations.clear()
        locations.addAll(data)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        holder.bind(remoteLocation = locations[position])
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        return LocationsViewHolder(
            ItemContainerLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class LocationsViewHolder(
        private val binding: ItemContainerLocationBinding
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(remoteLocation: RemoteLocation) {
                with(remoteLocation) {
                    val location = "$name, $region, $country"
                    binding.textRemoteLocation.text = location
                    binding.root.setOnClickListener {
                        onLocationClicked(remoteLocation)
                    }
                }
            }
    }


}