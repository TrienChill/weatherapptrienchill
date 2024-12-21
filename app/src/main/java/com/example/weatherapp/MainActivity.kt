package com.example.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.databinding.ActivityMainBinding

import androidx.appcompat.app.ActionBarDrawerToggle

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Đặt Toolbar làm ActionBar
        setSupportActionBar(binding.appBarMain.toolbar)

        // Lấy NavHostFragment và NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // Cấu hình AppBarConfiguration để hỗ trợ DrawerLayout
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment), // Các top-level destinations
            binding.drawerLayout
        )

        // Thiết lập Toolbar với NavController
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Thiết lập NavigationView để hoạt động với NavController
        binding.navView.setupWithNavController(navController)

        // Thêm ActionBarDrawerToggle để hiển thị Navigation Drawer
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.appBarMain.toolbar,
            R.string.drawer_open, R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

