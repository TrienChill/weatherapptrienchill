package com.example.weatherapp.fragments.manager_location

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherapp.data.RemoteLocation

class LocationDiffCallback(
    private val oldList: List<RemoteLocation>,
    private val newList: List<RemoteLocation>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // So sánh các địa điểm dựa trên một thuộc tính duy nhất, ví dụ như tên
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // So sánh toàn bộ đối tượng để xác định nội dung có thay đổi không
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
