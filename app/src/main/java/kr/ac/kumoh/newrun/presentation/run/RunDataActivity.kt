package kr.ac.kumoh.newrun.presentation.run

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kumoh.newrun.domain.data.DISTANCE_DATA_INFO
import kr.ac.kumoh.newrun.domain.data.LOCATION_STOP
import kr.ac.kumoh.newrun.domain.data.MyLocation
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.data.model.RunResultRequest
import kr.ac.kumoh.newrun.data.repository.RecordRunningService
import kr.ac.kumoh.newrun.domain.data.TIME_DATA_INFO
import kr.ac.kumoh.newrun.domain.data.TIME_RECORD
import kr.ac.kumoh.newrun.domain.model.TimeRecordData
import kr.ac.kumoh.newrun.domain.data.VELOCITY_DATA_INFO
import kr.ac.kumoh.newrun.databinding.ActivityRunDataBinding
import kr.ac.kumoh.newrun.domain.data.UserInfo
import kr.ac.kumoh.newrun.service.MyLocationService


class RunDataActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityRunDataBinding
    private var time: Int? = null
    private var velocity: Double? = null
    private var distance: Double? = null
    private var timeRecord: Array<TimeRecordData>? = null
    private lateinit var mMap: GoogleMap

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        time = intent.getIntExtra(TIME_DATA_INFO, -1)
        velocity = intent.getDoubleExtra(VELOCITY_DATA_INFO, -1.0)
        distance = intent.getDoubleExtra(DISTANCE_DATA_INFO, -1.0)
        timeRecord = intent.getParcelableArrayExtra(TIME_RECORD, TimeRecordData::class.java)
        var record = "("
        timeRecord?.forEach {
            record += "${it.latitude} ${it.longitude}, "
        }
        if(timeRecord?.size!! >0){
            record = record.substring(0, record.length - 2)
        }
        record += ")"


        binding.kmValueTextView.text = distance.toString()
        binding.velocityValueTextView.text = String.format("%.2f",velocity)
        binding.timeValueTextView.text = String.format("%02d:%02d", time!!/60%60, time!!%60)
        binding.calorieValueTextView.text = (distance!!*(time!!/60.0/60.0)*900).toString()


        binding.stopButton.setOnClickListener {
            val intent = Intent(this, MyLocationService::class.java).apply{action = LOCATION_STOP }
            startService(intent)
            RunActivity.activity?.finish()
            Log.d("테스트" , "보내는 값 : ${RunResultRequest(distance = distance.toString(), speed = velocity.toString(), run_time = String.format("%02d:%02d", time!!/60, time!!%60), userEmail = UserInfo.userEmail, route = record)}")
            CoroutineScope(Dispatchers.IO).launch{
                val message= RecordRunningService().recordRunning(RunResultRequest(distance = distance.toString(), speed = velocity.toString(), run_time = String.format("%02d:%02d:%02d", time!!/60/60, time!!/60%60, time!!%60), userEmail = UserInfo.userEmail, route = record))
                Log.d("테스트", "메시지 반환값 : ${message.message}")
            }
            binding.playButton.visibility = View.GONE
            binding.stopButton.visibility = View.GONE
            binding.shareButton.visibility = View.VISIBLE
            binding.exitButton.visibility = View.VISIBLE
        }

        binding.exitButton.setOnClickListener {
            finish()
        }
        binding.shareButton.setOnClickListener {

        }

        binding.playButton.setOnClickListener {
            finish()
        }

        startMap()
    }

    private fun startMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.runMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        updateLocation()
    }

    private fun updateLocation() {

        val bigPictureBitmap = BitmapFactory.decodeResource(resources, R.drawable.marker)

        val latLng = LatLng(MyLocation.myLatitude!!, MyLocation.myLongitude!!)

        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(15.0f)
            .build()
        mMap.clear()
        mMap.addMarker(
            MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bigPictureBitmap))
                .position(LatLng(MyLocation.myLatitude!!, MyLocation.myLongitude!!))
                .zIndex(2f)
        )
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        drawRoute()
    }

    private fun drawRoute(){
        val polylineOptions = PolylineOptions()
        for(point in timeRecord!!){
            polylineOptions.add(LatLng(point.latitude, point.longitude))
        }
        val polyline = mMap.addPolyline(polylineOptions)
        polyline.width = 20.0f
        polyline.color = R.color.main_color
    }

}