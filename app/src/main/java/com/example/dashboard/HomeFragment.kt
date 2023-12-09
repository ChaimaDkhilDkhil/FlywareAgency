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
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging


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
 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewList = view.findViewById(R.id.view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapContainer) as SupportMapFragment?
            ?: SupportMapFragment.newInstance().also {
                childFragmentManager.beginTransaction().replace(R.id.mapContainer, it).commit()
                it.getMapAsync(this)
            }
        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Fetch location and display it on the map
        fetchLocation()

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
            // Request location permission if not granted
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_CODE
            )
        } else {
            // Permission already granted, fetch location
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        currentLocation = location
                        // Display the location on the map
                        googleMap?.let {
                            showLocationOnMap(it)
                        }
                    } else {
                        Log.e("Location", "Null location received.")
                        // If location is null, manually set the default location to Tunis
                        showLocationOnMap(googleMap!!)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Location", "Error getting location: ${e.message}")
                    // If there is an error fetching the location, manually set the default location to Tunis
                    showLocationOnMap(googleMap!!)
                }
        }
    }

    private fun showLocationOnMap(googleMap: GoogleMap) {
        // Display the location on the map
        val tunisLatLng = LatLng(36.8065, 10.1815) // Coordinates for Tunis
        val markerOptions = MarkerOptions().position(tunisLatLng).title("Tunis")

        googleMap.clear() // Clear any existing markers
        googleMap.addMarker(markerOptions)

        // Center the map on Tunis
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tunisLatLng, 12f))
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        // Center the map on Tunisia
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