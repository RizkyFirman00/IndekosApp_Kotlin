package com.example.indekos.ui.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.indekos.R
import com.example.indekos.databinding.ActivityMapsBinding
import com.example.indekos.databinding.DialogSetRadiusBinding
import com.example.indekos.model.Indekos
import com.example.indekos.ui.detail.DetailActivity
import com.example.indekos.util.ViewModelFactory
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var circle: Circle? = null
    private val markerList: MutableList<Marker> = mutableListOf()
    private var userLocation = LatLng(0.0, 0.0)
    private var radiusValue: Double = 3000.0
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val options = GoogleMapOptions()
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
            .compassEnabled(true)
            .rotateGesturesEnabled(true)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
        mMap.uiSettings
            .isMyLocationButtonEnabled = false

        mMap.setOnInfoWindowClickListener { marker ->
            val indekosId = marker.tag
            val id = indekosId.toString()
            showIndekosDetail(id)
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val latUser = intent.getDoubleExtra("latUser", 0.0)
        val longUser = intent.getDoubleExtra("longUser", 0.0)
        Log.d("MapsActivity", "Check latLng = lat: $latUser, lng: $longUser")

        val myLocation = LatLng(latUser, longUser)
        userLocation = myLocation
        Log.d("MapsActivity", "Check latLng = $myLocation")

        binding.btnSetRadius.setOnClickListener {
            showRadiusDialog()
        }

        binding.btnSearch.setOnClickListener {
            searchLocation()
        }

        binding.btnMyLocation.setOnClickListener {
            userCurrentLocation()
        }

        lifecycleScope.launch {
            viewModel.getAllIndekos()
            viewModel.indekosList.observe(this@MapsActivity) { indekosList ->
                if (indekosList.isNullOrEmpty()) {
                    Toast.makeText(
                        this@MapsActivity,
                        "Tidak ada data indekos yang tersedia",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    filterIndekosByRadius()
                    updateCircleRadius(radiusValue)
                }
                mMap.moveCamera(
                    CameraUpdateFactory
                        .newLatLngZoom(myLocation, 15f)
                )
            }
        }
    }

    private fun calculateDistance(startLatLng: LatLng, endLatLng: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            startLatLng.latitude,
            startLatLng.longitude,
            endLatLng.latitude,
            endLatLng.longitude,
            results
        )
        return results[0]
    }

    private fun showRadiusDialog() {
        val dialogBinding = DialogSetRadiusBinding.inflate(layoutInflater)
        val dialogView = dialogBinding.root

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Set Radius")
            .setView(dialogView)
            .setPositiveButton("Set") { _, _ ->
                val radius = dialogBinding.etSetRadius.text.toString().toDoubleOrNull()
                if (radius != null) {
                    radiusValue = radius
                    filterIndekosByRadius()
                    updateCircleRadius(radius)
                } else {
                    Toast.makeText(this, "Invalid radius value", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.show()
    }

    private fun updateCircleRadius(radius: Double) {
        circle?.remove()

        val circleOptions = CircleOptions()
            .center(userLocation)
            .radius(radius)
            .strokeWidth(2f)
            .strokeColor(getColor(R.color.purple))
            .fillColor(getColor(R.color.transparent_white))

        circle = mMap.addCircle(circleOptions)
    }

    private fun filterIndekosByRadius() {
        clearMarkers()
        val filteredIndekosList = viewModel.indekosList.value?.filter { indekos ->
            val indekosLocation = LatLng(
                indekos.latitude_indekos ?: 0.0,
                indekos.longitude_indekos ?: 0.0
            )
            if (radiusValue == null) {
                true // Tampilkan semua indekos jika radius belum diatur
            } else {
                val distance = calculateDistance(userLocation, indekosLocation)
                distance <= radiusValue // Filter indekos yang berjarak kurang dari atau sama dengan radius yang diinginkan
            }
        }?.toMutableList()
        if (filteredIndekosList.isNullOrEmpty()) {
            Toast.makeText(
                this@MapsActivity,
                "Tidak ada indekos\ndalam radius $radiusValue meter",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            mMap.clear()
            Toast.makeText(
                this@MapsActivity,
                "Menampilkan ${filteredIndekosList.size} indekos\ndalam radius $radiusValue meter",
                Toast.LENGTH_SHORT
            ).show()
            filteredIndekosList.forEach { indekos ->
                val indekosLocation = LatLng(
                    indekos.latitude_indekos ?: 0.0,
                    indekos.longitude_indekos ?: 0.0
                )
                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(indekosLocation)
                        .title(indekos.name_indekos)
                )
                if (marker != null) {
                    markerList.add(marker)
                    marker.snippet = "Harga : ${indekos.harga} / bulan"
                    marker.tag = indekos.indekosId
                }
            }
        }
    }

    private fun searchLocation() {
        val locationName = binding.etSearch.text.toString()
        val geocoder = Geocoder(this)
        val addressList: MutableList<Address>? = geocoder.getFromLocationName(locationName, 1)
        if (!addressList.isNullOrEmpty()) {
            val address = addressList[0]
            val latLng = LatLng(address.latitude, address.longitude)
            userLocation = latLng
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            filterIndekosByRadius()
            updateCircleRadius(radiusValue)
        } else {
            Toast.makeText(this, "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun userCurrentLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    userLocation = latLng
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    filterIndekosByRadius()
                    updateCircleRadius(radiusValue)
                } else {
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    private fun showIndekosDetail(indekosId: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("indekosId", indekosId)
        startActivity(intent)
    }

    private fun clearMarkers() {
        for (marker in markerList) {
            marker.remove()
        }
        markerList.clear()
    }
}