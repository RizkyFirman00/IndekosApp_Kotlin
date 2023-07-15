package com.example.indekos.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.indekos.databinding.ActivityMainBinding
import com.example.indekos.model.Indekos
import com.example.indekos.ui.detail.DetailActivity
import com.example.indekos.ui.map.MapsActivity
import com.example.indekos.ui.splash.SplashScreenActivity
import com.example.indekos.util.ViewModelFactory
import com.example.indekos.util.adapter.IndekosHomeAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class HomeActivity : AppCompatActivity() {

    override fun onBackPressed() {
        val intent = Intent(this, SplashScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var userLocation: Location
    private var latUser: Double? = null
    private var longUser: Double? = null
    private lateinit var adapter: IndekosHomeAdapter
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(1000) // Interval pembaruan lokasi dalam milidetik

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    val lastLocation = locationResult.lastLocation
                    val lat = lastLocation?.latitude
                    val long = lastLocation?.longitude
                    latUser = lat
                    longUser = long
                    Log.d("HomeActivity", "User Location: $latUser, $longUser")
                    updateUserLocation()
                }
            }
        }

        getAccessLocation()

        binding.btnOtherLocation.setOnClickListener {
            if (latUser != null && longUser != null) {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("latUser", latUser)
                intent.putExtra("longUser", longUser)
                Log.d("HomeActivity", "Check location: $latUser, $longUser")
                stopLocationUpdates()
                startActivity(intent)
            } else {
                Toast.makeText(this, "Tidak dapat mengambil lokasi pengguna", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnMyLocation.setOnClickListener {
            startLocationUpdates()
            Toast.makeText(this, "Lokasi Anda diperbarui : Lokasi terkini", Toast.LENGTH_SHORT).show()
        }

        adapter = IndekosHomeAdapter(
            onItemClick = {
                navigateToDetailActivity(it)
                Log.d("HomeActivity", "IndekosId: $it")
            }
        )
        binding.rvKost.adapter = adapter
        binding.rvKost.layoutManager = LinearLayoutManager(this)
        viewModel.getAllIndekos()
        viewModel.indekosList.observe(this) { indekosList ->
            if (indekosList.isNullOrEmpty()) {
                Toast.makeText(this, "Tidak ada data indekos", Toast.LENGTH_SHORT).show()
                binding.progressBar2.visibility = View.VISIBLE
            } else {
                binding.progressBar2.visibility = View.GONE
                adapter.submitList(indekosList)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun updateUserLocation() {
        latUser?.let { lat ->
            longUser?.let { long ->
                userLocation = Location("User")
                userLocation.latitude = lat
                userLocation.longitude = long
                Log.d("HomeActivity", "updateUserLocation: $userLocation")
                updateIndekosList()
            }
        }
    }

    fun Indekos.calculateDistance(latUser: Double, longUser: Double): String {
        val earthRadius = 6371 // Radius bumi dalam kilometer
        val latDistance = Math.toRadians(latUser - latitude_indekos!!)
        val longDistance = Math.toRadians(longUser - longitude_indekos!!)
        val a = sin(latDistance / 2).pow(2) + (cos(Math.toRadians(latitude_indekos)) *
                cos(Math.toRadians(latUser)) * sin(longDistance / 2).pow(2))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distanceInKm = earthRadius * c
        val distanceInMeter = distanceInKm * 1000
        return String.format("%.1f", distanceInMeter)
    }

    private fun updateIndekosList() {
        val sortedList = viewModel.indekosList.value?.sortedBy { indekos ->
            val indekosLocation = Location("Indekos")
            indekosLocation.latitude = indekos.latitude_indekos ?: 0.0
            indekosLocation.longitude = indekos.longitude_indekos ?: 0.0
            userLocation.distanceTo(indekosLocation)
        }
        sortedList?.let { adapter.submitList(it) }

        sortedList?.forEachIndexed { index, indekos ->
            val distance = indekos.calculateDistance(
                latUser ?: 0.0,
                longUser ?: 0.0,
            )
            adapter.updateJarak(indekos.indekosId, distance)
            Log.d("HomeActivity", "Jarak: $index, $distance m")
        }
    }

    private fun navigateToDetailActivity(indekosId: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("indekosId", indekosId)
        startActivity(intent)
    }

    private fun getAccessLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getAccessLocation()
            }

            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getAccessLocation()
            }

            else -> {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}