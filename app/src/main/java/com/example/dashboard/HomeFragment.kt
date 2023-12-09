package com.example.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: android.location.Location? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var recyclerViewList: RecyclerView? = null
    private var googleMap: GoogleMap? = null
    private var lastKnownLocation: LatLng? = null
    private var isMapInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the map only if it hasn't been initialized before
        if (!isMapInitialized) {
            recyclerViewList = view.findViewById(R.id.view)
            val mapFragment = childFragmentManager.findFragmentById(R.id.mapContainer) as SupportMapFragment?
                ?: SupportMapFragment.newInstance().also {
                    childFragmentManager.beginTransaction().replace(R.id.mapContainer, it).commit()
                    it.getMapAsync(this)
                }

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
            fetchLocation()
            isMapInitialized = true
        }

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewList?.layoutManager = linearLayoutManager

        val news = ArrayList<ListDomain>()
        news.add(ListDomain("Browsing trips in Belgium", "pic1"))
        news.add(ListDomain("book a flight to Tunisia", "tunisia3"))
        news.add(ListDomain("Korea booking offer", "corea3"))
        news.add(ListDomain("Stays in Rome", "roma2"))
        adapter = NewsAdapter(news)
        recyclerViewList?.adapter = adapter

        val flightButton = view.findViewById<Button>(R.id.flightButton)
        flightButton.setOnClickListener {
            val intent = Intent(requireContext(), FlightActivity::class.java)
            startActivity(intent)
        }

        val staysButton = view.findViewById<Button>(R.id.StaysButton)
        staysButton.setOnClickListener {
            val intent = Intent(requireContext(), staysActivity::class.java)
            startActivity(intent)
        }
        val transportsButton = view.findViewById<Button>(R.id.transportButton)
        transportsButton.setOnClickListener {
            val intent = Intent(requireContext(), transportsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Fetch location when the fragment is resumed (e.g., after returning from another fragment)
        fetchLocation()
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_CODE
            )
        } else {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        currentLocation = location
                        lastKnownLocation = LatLng(location.latitude, location.longitude)
                        googleMap?.let { map ->
                            showLocationOnMap(map)
                        }
                    } else {
                        Log.e("Location", "Null location received.")
                        googleMap?.let { map ->
                            showLocationOnMap(map, lastKnownLocation ?: LatLng(36.8065, 10.1815))
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Location", "Error getting location: ${e.message}")
                    googleMap?.let { map ->
                        showLocationOnMap(map, lastKnownLocation ?: LatLng(36.8065, 10.1815))
                    }
                }
        }
    }

    private fun showLocationOnMap(googleMap: GoogleMap, location: LatLng? = null) {
        val targetLocation = location ?: LatLng(36.8065, 10.1815) // Default to Tunisia if location is null
        val markerOptions = MarkerOptions().position(targetLocation).title("Tunis")

        googleMap.clear()
        googleMap.addMarker(markerOptions)

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, 12f))
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        val tunisiaCenter = LatLng(33.8869, 9.5375)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(tunisiaCenter))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("Permission", "Permission result received: $requestCode")
        if (requestCode == REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLocation()
        }
    }

    companion object {
        private const val REQUEST_CODE = 101
    }
}
