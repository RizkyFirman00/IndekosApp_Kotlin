package com.example.indekos.ui.detailHistory

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.indekos.R
import com.example.indekos.databinding.ActivityDetailHistoryBinding
import com.example.indekos.ui.history.HistoryActivity
import com.example.indekos.util.Preferences
import com.example.indekos.util.ViewModelFactory
import com.example.indekos.util.adapter.PhotosAdapterDetail
import com.example.indekos.util.createCustomTempFile
import com.example.indekos.util.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val binding by lazy { ActivityDetailHistoryBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<DetailHistoryViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private var latIndekos: Double = 0.0
    private var longIndekos: Double = 0.0
    private var file: File? = null
    private lateinit var photoPath: String
    private lateinit var photoAdapter: PhotosAdapterDetail
    private val photoList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        val indekosId = intent.getStringExtra("indekosId")
        val userId = Preferences.getUserId(this)

        if (indekosId != null) {
            viewModel.getIndekosById(indekosId.toInt()).observe(this) {
                binding.etNamaIndekos.setText(it.name_indekos)
                binding.etHargaPerBulan.setText(it.harga)
                binding.etJumlahBedroom.setText(it.jumlah_bedroom)
                binding.etJumlahCupboard.setText(it.jumlah_cupboard)
                binding.etJumlahKitchen.setText(it.jumlah_kitchen)
                binding.etAlamat.setText(it.alamat)
                binding.etKota.setText(it.kota)
                binding.etProvinsi.setText(it.provinsi)
                binding.etLokasi.setText("${it.latitude_indekos}, ${it.longitude_indekos}")
                Glide.with(this)
                    .load(it.photoBannerUrl)
                    .into(binding.ivPhotoBanner)
                file = File(it.photoBannerUrl)
                it.photoUrl?.let { it1 -> photoList.addAll(it1) }
                photoAdapter = PhotosAdapterDetail(photoList)
                binding.rvPhotos.apply {
                    layoutManager = LinearLayoutManager(
                        this@DetailHistoryActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    adapter = photoAdapter
                }
            }
        }

        binding.btnBack.setOnClickListener {
            Intent(this, HistoryActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        binding.btnUpdateData.setOnClickListener {
            val namaIndekos = binding.etNamaIndekos.text.toString()
            val hargaPerBulan = binding.etHargaPerBulan.text.toString()
            val jumlahBedroom = binding.etJumlahBedroom.text.toString()
            val jumlahCupboard = binding.etJumlahCupboard.text.toString()
            val jumlahKitchen = binding.etJumlahKitchen.text.toString()
            val alamat = binding.etAlamat.text.toString()
            val kota = binding.etKota.text.toString()
            val provinsi = binding.etProvinsi.text.toString()
            val lokasi = binding.etLokasi.text.toString()
            val file = file

            if (namaIndekos.isEmpty() || hargaPerBulan.isEmpty() || jumlahBedroom.isEmpty() || jumlahCupboard.isEmpty() || jumlahKitchen.isEmpty() || alamat.isEmpty() || kota.isEmpty() || provinsi.isEmpty() || lokasi.isEmpty()) {
                Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                if (indekosId != null && userId != null) {
                    viewModel.updateIndekos(
                        indekosId.toInt(),
                        userId.toInt(),
                        namaIndekos,
                        hargaPerBulan,
                        jumlahBedroom,
                        jumlahCupboard,
                        jumlahKitchen,
                        latIndekos,
                        longIndekos,
                        alamat,
                        kota,
                        provinsi,
                        photoList,
                        file.toString()
                    )
                }
                Toast.makeText(this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
                Intent(this, HistoryActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }

        binding.btnDeleteData.setOnClickListener {
            Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            Intent(this, HistoryActivity::class.java).also {
                startActivity(it)
                finish()
            }
            indekosId?.let { viewModel.deleteIndekos(it.toInt()) }
        }

        // Button check lokasi
        binding.btnCheckLokasi.setOnClickListener {
            binding.etLokasi.setText("$latIndekos, $longIndekos")
            binding.etLokasi.isEnabled = false
            Toast.makeText(this, "Lokasi berhasil diambil", Toast.LENGTH_SHORT).show()
        }

        // Button tambah foto banner
        binding.btnPhotoBannerCamera.setOnClickListener {
            startCamera()
        }
        binding.btnPhotoBannerGallery.setOnClickListener {
            startGallery()
        }

        // Button tambah foto indekos
        photoAdapter = PhotosAdapterDetail(photoList)
        binding.rvPhotos.apply {
            layoutManager = LinearLayoutManager(
                this@DetailHistoryActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = photoAdapter
            photoAdapter.setOnPhotoDeleteLongClickListener(object :
                PhotosAdapterDetail.OnItemLongClickListener {
                override fun onItemLongClick(position: Int) {
                    photoList.removeAt(position)
                    photoAdapter.notifyDataSetChanged()
                }
            })
        }
        binding.btnPhotosGallery.setOnClickListener {
            showPhotoSelectionDialog()
        }

        // Logika Currency Formatter
        val etHargaPerBulan: EditText = binding.etHargaPerBulan
        etHargaPerBulan.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (isUpdating) {
                    return
                }

                isUpdating = true

                val originalText = s.toString()

                // Hapus semua tanda titik sebelum memformat angka
                val cleanText = originalText.replace(".", "")

                // Format ulang angka dengan menambahkan titik setiap 3 angka
                val formattedText = formatCurrency(cleanText)

                // Set teks yang telah diformat ke EditText
                etHargaPerBulan.setText(formattedText)

                // Posisikan kursor di akhir teks
                etHargaPerBulan.setSelection(formattedText.length)

                isUpdating = false
            }

            private fun formatCurrency(value: String): String {
                // Hapus tanda minus jika ada
                var isNegative = false
                var cleanValue = value
                if (cleanValue.startsWith("-")) {
                    isNegative = true
                    cleanValue = cleanValue.substring(1)
                }

                // Format ulang angka dengan menambahkan titik setiap 3 angka
                val stringBuilder = StringBuilder(cleanValue)
                val length = stringBuilder.length
                var i = length - 3
                while (i > 0) {
                    stringBuilder.insert(i, ".")
                    i -= 3
                }

                // Tambahkan tanda minus kembali jika angka negatif
                if (isNegative) {
                    stringBuilder.insert(0, "-")
                }

                return stringBuilder.toString()
            }
        })

        // Request location permissions if not granted
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Get location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLocation()
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_CODE_PERMISSIONS
            )
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    latIndekos = location.latitude
                    longIndekos = location.longitude
                } else if (latIndekos == 0.0 && longIndekos == 0.0) {
                    Toast.makeText(
                        this,
                        "Location not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // Rv Photo Function
    private fun showPhotoSelectionDialog() {
        val options = arrayOf<CharSequence>("Ambil dari kamera", "Ambil dari galeri")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Metode Anda :")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Ambil dari kamera" -> {
                    startCameraPhotos()
                }

                options[item] == "Ambil dari galeri" -> {
                    startGalleryPhotos()
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    //Gallery Function Rv Photos
    private fun startGalleryPhotos() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGalleryPhotos.launch(chooser)
    }

    private val launcherIntentGalleryPhotos = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            val photoFilePath = myFile.absolutePath
            photoList.add(photoFilePath)
            photoAdapter.notifyDataSetChanged()
        }
    }

    //Camera Function Rv Photos
    @SuppressLint("QueryPermissionsNeeded")
    private fun startCameraPhotos() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this, "com.example.indekos", it
            )
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCameraPhotos.launch(intent)
        }
    }

    private val launcherIntentCameraPhotos = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(photoPath)
            val photoFilePath = myFile.absolutePath
            photoList.add(photoFilePath)
            photoAdapter.notifyDataSetChanged()
        }
    }

    //Gallery Function
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            file = myFile
            Glide.with(this)
                .load(selectedImg)
                .into(binding.ivPhotoBanner)
        }
    }

    //Camera Function
    @SuppressLint("QueryPermissionsNeeded")
    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this, "com.example.indekos", it
            )
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(photoPath)
            file = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            Glide.with(this)
                .load(result)
                .into(binding.ivPhotoBanner)
        }
    }

    //Location Function
    private fun getMyLocation() {
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
                getMyLocation()
            }

            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getMyLocation()
            }

            else -> {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Permission Function
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this, getString(R.string.permission_not_granted), Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 123
    }
}