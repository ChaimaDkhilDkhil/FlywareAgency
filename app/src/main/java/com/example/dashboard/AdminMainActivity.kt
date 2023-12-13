package com.example.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminMainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)
        bottomNavigationView = findViewById(R.id.adminbottomnavigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_flight -> {
                    replaceFragment(AdminFlightFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_hotel -> {
                    replaceFragment(AdminHotelFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_transport -> {
                    replaceFragment(AdminTransportFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_profile -> {
                    replaceFragment(ProfileFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        replaceFragment(AdminFlightFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()
    }}