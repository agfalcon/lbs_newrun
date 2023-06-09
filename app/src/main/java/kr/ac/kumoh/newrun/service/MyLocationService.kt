package kr.ac.kumoh.newrun.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kr.ac.kumoh.newrun.domain.data.ACTION_LOCATION_UPDATE
import kr.ac.kumoh.newrun.domain.data.ACTION_RUN_UPDATE
import kr.ac.kumoh.newrun.domain.data.DISTANCE_DATA_INFO
import kr.ac.kumoh.newrun.domain.data.LOCATION_DATA_INFO
import kr.ac.kumoh.newrun.domain.data.LOCATION_START
import kr.ac.kumoh.newrun.domain.data.LOCATION_STOP
import kr.ac.kumoh.newrun.domain.data.MyLocation
import kr.ac.kumoh.newrun.domain.data.RUN_PAUSE
import kr.ac.kumoh.newrun.domain.data.RUN_RESUME
import kr.ac.kumoh.newrun.domain.data.RUN_START
import kr.ac.kumoh.newrun.domain.data.RunningData
import kr.ac.kumoh.newrun.domain.data.RunningData.time
import kr.ac.kumoh.newrun.domain.data.TIME_DATA_INFO
import kr.ac.kumoh.newrun.domain.data.TIME_RECORD
import kr.ac.kumoh.newrun.domain.model.TimeRecordData
import kr.ac.kumoh.newrun.domain.data.VELOCITY_DATA_INFO
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.*

class MyLocationService : Service() {

    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient




    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = 2000 // Update location every 10 seconds
            fastestInterval = 1000 // Fastest interval for updates
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Use GPS if available
        }
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            LOCATION_START -> {
                Log.d("테스트", "service: Location start")
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
            LOCATION_STOP -> {
                Log.d("테스트", "service: Location stop")
                fusedLocationClient.removeLocationUpdates(locationCallback)
                RunningData.timer?.cancel()
                RunningData.timeRecord = mutableListOf()
                RunningData.reset()
                stopSelf()
            }
            RUN_START -> {
                Log.d("테스트", "service: run start")
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
                RunningData.timeRecord.add(TimeRecordData(0, MyLocation.myLatitude!!, MyLocation.myLongitude!!))
                timeCheckStart()
            }
            RUN_PAUSE -> {
                Log.d("테스트", "service: run pause")
                fusedLocationClient.removeLocationUpdates(locationCallback)
                RunningData.timer?.cancel()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun timeCheckStart(){
        Log.d("테스트", "timer 시작")
        RunningData.timer = timer(initialDelay = 0, period = 1000){
            time +=1
            RunningData.distance += round(getDistance(MyLocation.myLatitude!!, MyLocation.myLongitude!!, RunningData.timeRecord.last().latitude, RunningData.timeRecord.last().longitude)/1000*100)/100
            RunningData.timeRecord.add(TimeRecordData(time, MyLocation.myLatitude!!, MyLocation.myLongitude!!))
            Log.d("테스트", "시간 : $time")
            Log.d("테스트", "거리 : ${RunningData.distance}")
            RunningData.velocity = round(RunningData.distance/time*60*60*100)/100
            Log.d("테스트", "속도 : ${RunningData.velocity}")
            Log.d("테스트", "위도, 경도: ${MyLocation.myLatitude}, ${MyLocation.myLongitude}")
            val intent = Intent(ACTION_RUN_UPDATE).apply {
                putExtra(TIME_DATA_INFO, time)
                putExtra(DISTANCE_DATA_INFO, RunningData.distance)
                putExtra(VELOCITY_DATA_INFO, RunningData.velocity)
                putExtra(TIME_RECORD, RunningData.timeRecord.toTypedArray())
            }
            sendBroadcast(intent)
        }
    }

    private fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
        val c = 2 * asin(sqrt(a))
        return (RunningData.R * c)
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation.apply {
                MyLocation.myLatitude = this!!.latitude
                MyLocation.myLongitude = this.longitude
                val intent = Intent(ACTION_LOCATION_UPDATE).apply {
                    putExtra(LOCATION_DATA_INFO, true)
                }
                LocalBroadcastManager.getInstance(this@MyLocationService).sendBroadcast(intent)
            }
        }
    }
}