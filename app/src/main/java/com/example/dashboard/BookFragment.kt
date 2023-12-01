package com.example.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BookFragment : Fragment() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book, container, false)

        bottomNavigationView = view.findViewById(R.id.bottomnavigationBooking)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_flight_booking -> {
                    replaceFragment(FlightBookingFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menu_transport_booking -> {
                    replaceFragment(transportBookingFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        replaceFragment(FlightBookingFragment())

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameBooking, fragment)
            .commit()
    }
}
