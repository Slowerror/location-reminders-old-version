package com.slowerror.locationreminders.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.slowerror.locationreminders.R
import com.slowerror.locationreminders.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigateUp()
    }

    private fun setupNavigateUp() {
        val navHost = supportFragmentManager.findFragmentById(R.id.navContainer) as NavHostFragment
        navController = navHost.navController

        appBarConfig = AppBarConfiguration(setOf(R.id.loginFragment, R.id.remindersFragment))
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.navContainer).navigateUp()
}