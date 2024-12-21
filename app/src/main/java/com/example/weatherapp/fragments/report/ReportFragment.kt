package com.example.weatherapp.fragments.report

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.weatherapp.R

class ReportFragment : Fragment(R.layout.fragment_report) {

    private lateinit var reportButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tìm nút trong layout
        reportButton = view.findViewById(R.id.button_report)

        // Thiết lập sự kiện nhấn nút
        reportButton.setOnClickListener {
            openGoogleForm()
        }
    }

    private fun openGoogleForm() {
        // Đường link đến Google Form để báo cáo sai vị trí
        val url = "https://forms.gle/UQjy491hJ5Pdjp126"

        // Tạo Intent để mở trình duyệt web
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
