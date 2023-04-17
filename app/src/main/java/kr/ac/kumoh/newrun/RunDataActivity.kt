package kr.ac.kumoh.newrun

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kr.ac.kumoh.newrun.databinding.ActivityRunDataBinding


class RunDataActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityRunDataBinding
    private var time: Int? = null
    private var velocity: Double? = null
    private var distance: Double? = null
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        time = intent.getIntExtra(TIME_DATA_INFO, -1)
        velocity = intent.getDoubleExtra(VELOCITY_DATA_INFO, -1.0)
        distance = intent.getDoubleExtra(DISTANCE_DATA_INFO, -1.0)

        binding.kmValueTextView.text = distance.toString()
        binding.velocityValueTextView.text = String.format("%.2f",velocity)
        binding.timeValueTextView.text = String.format("%02d:%02d", time!!/60, time!!%60)

        binding.stopButton.setOnClickListener {
            val intent = Intent(this, MyLocationService::class.java).apply{action = LOCATION_STOP}
            startService(intent)
            binding.playButton.visibility = View.GONE
            binding.stopButton.visibility = View.GONE
            binding.shareButton.visibility = View.VISIBLE
        }

        binding.playButton.setOnClickListener {
            finish()
        }

        startMap()
    }

    fun startMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.runMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStop() {
        super.onStop()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        updateLocation()
    }

    fun updateLocation() {

        val bigPictureBitmap = BitmapFactory.decodeResource(resources, R.drawable.marker)

        val LATLNG = LatLng(MyLocation.myLatitude!!, MyLocation.myLongitude!!)

        val cameraPosition = CameraPosition.Builder()
            .target(LATLNG)
            .zoom(15.0f)
            .build()
        mMap.clear()
        mMap.addMarker(
            MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bigPictureBitmap))
                .position(LatLng(MyLocation.myLatitude!!, MyLocation.myLongitude!!))
                .zIndex(2f)
        )
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}