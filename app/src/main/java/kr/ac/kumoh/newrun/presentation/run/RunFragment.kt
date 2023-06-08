package kr.ac.kumoh.newrun.presentation.run

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.helper.widget.Layer
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kr.ac.kumoh.newrun.domain.data.ACTION_LOCATION_UPDATE
import kr.ac.kumoh.newrun.domain.data.LOCATION_DATA_INFO
import kr.ac.kumoh.newrun.domain.data.LOCATION_START
import kr.ac.kumoh.newrun.domain.data.LOCATION_STOP
import kr.ac.kumoh.newrun.domain.data.MyLocation
import kr.ac.kumoh.newrun.domain.data.PERMISSIONS_REQUEST_ACCESS_LOCATION
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.databinding.DialogCountdownSettingBinding
import kr.ac.kumoh.newrun.service.MyLocationService
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.hypot


class RunFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap : MapView
    private lateinit var googleMap: GoogleMap
    private var myMarker : Marker? = null
    private var countDownSecond = 3
    private lateinit var timerValueTextView: TextView
    private lateinit var timerLayer: Layer
    private lateinit var startButton: Button
    private lateinit var musicButton: Button
    private lateinit var countDownTextView : TextView
    private var musicOn = true
    private var timer: Timer? = null

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getBooleanExtra(LOCATION_DATA_INFO, false)
            if (location) {
                if(myMarker == null){
                    val bigPictureBitmap = BitmapFactory.decodeResource(resources,
                        R.drawable.marker
                    )
                    myMarker = googleMap.addMarker(
                        MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bigPictureBitmap))
                            .position(LatLng(MyLocation.myLatitude!!, MyLocation.myLongitude!!))
                            .title("나의 위치")
                            .zIndex(2f)
                    )
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarker!!.position, 16f))
                }
                else{
                    myMarker?.position = LatLng(MyLocation.myLatitude!!, MyLocation.myLongitude!!)
                }
            }
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        LocalBroadcastManager.getInstance(activity!!).registerReceiver(locationReceiver, IntentFilter(
            ACTION_LOCATION_UPDATE
        ))
        getLocationPermission()

        return  setFragment(inflater, container, savedInstanceState)
    }

    private fun showCountDownSettingDialog(){
        AlertDialog.Builder(requireActivity()).apply{
            val dialogBinding = DialogCountdownSettingBinding.inflate(layoutInflater)
            with(dialogBinding.countDownSecondPicker){
                maxValue = 10
                minValue = 0
                value = countDownSecond
            }
            setTitle("카운트다운 설정")
            setView(dialogBinding.root)
            setPositiveButton("설정") {_,_ ->
                countDownSecond = dialogBinding.countDownSecondPicker.value
                timerValueTextView.text = "${countDownSecond}sec"
            }
            setNegativeButton("취소", null)
        }.show()
    }

    private fun setFragment(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?) : View?{
        val rootView = inflater.inflate(R.layout.fragment_run,container,false)
        mMap = rootView.findViewById(R.id.map) as MapView
        mMap.onCreate(savedInstanceState)
        mMap.getMapAsync(this)
        setView(rootView)
        return rootView
    }

    private fun setView(rootView: View?) {
        rootView?.apply {
            timerLayer = findViewById(R.id.timerLayer)
            startButton = findViewById(R.id.startButton)
            musicButton = findViewById(R.id.musicButton)
            timerValueTextView = findViewById(R.id.timerValueTextView)
            countDownTextView = findViewById(R.id.countDownTextView)
        }
        timerLayer.setOnClickListener {
            showCountDownSettingDialog()
        }
        musicButton.setOnClickListener {
            musicOn = !musicOn
            musicButton.isSelected = musicOn
        }
        startButton.setOnClickListener {
            startRunning()
        }
    }

    private fun startRunning(){
        var  currentCount = countDownSecond + 1
        timer = timer(initialDelay = 0, period = 1000){
            requireActivity().runOnUiThread { countDownTextView.visibility = View.VISIBLE}
            currentCount -= 1
            if(currentCount ==0) {
                requireActivity().runOnUiThread { countDownTextView.visibility = View.INVISIBLE}
                timer?.cancel()
                val intent = Intent(activity, MyLocationService::class.java)
                    .apply{ action = LOCATION_STOP }
                requireActivity().startService(intent)
                val runIntent = Intent(activity, RunActivity::class.java)
                startActivity(runIntent)
            } else{
                requireActivity().runOnUiThread {countDownTextView.text = currentCount.toString()}
                animateText()
            }
//            if(currentCount <=3){
//                val toneType = if(currentCount ==0) ToneGenerator.TONE_CDMA_HIGH_L else ToneGenerator.TONE_CDMA_ANSWER
//                ToneGenerator(AudioManager.STREAM_ALARM, 40)
//                    .startTone(toneType, 100)
//            }
        }
        Log.d("테스트", "타이머 끝")
    }

    private fun animateText(){
        val cx: Int = countDownTextView.width /2
        val cy: Int = countDownTextView.height / 2

        val initialRadius: Float = hypot(cx.toFloat(), cy.toFloat())
        val anim: Animator = ViewAnimationUtils.createCircularReveal(countDownTextView, cx, cy, initialRadius, 0f)
        requireActivity().runOnUiThread { countDownTextView.visibility = View.VISIBLE}
        val mHandler = Handler(Looper.getMainLooper())
        mHandler.postDelayed(Runnable {
            anim.start()
        }, 0)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.setAllGesturesEnabled(false)
        googleMap.uiSettings.isZoomControlsEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.uiSettings.isZoomControlsEnabled = false
        val location = LatLng(36.145097, 128.393358)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(16f))
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                activity!!,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("테스트", "location 실행")
            getMyLocation()
        } else {
            Log.d("테스트", "권한 요청 실행")
            requestAccessLocationPermission()
        }
    }


    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        val intent = Intent(activity, MyLocationService::class.java)
            .apply{ action = LOCATION_START }
        requireActivity().startService(intent)
    }

    private fun requestAccessLocationPermission() {
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
        val contract = ActivityResultContracts.RequestMultiplePermissions()
        val activityResultLauncher = registerForActivityResult(contract) { resultMap ->
            val isAllGranted = permissions.all { e -> resultMap[e] == true }
            if (isAllGranted) {
                getMyLocation()
            }
        }
        activityResultLauncher.launch(permissions)
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_LOCATION -> {
                if (grantResults.isNotEmpty()) {
                    for ((i, permission) in permissions.withIndex()) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(
                                activity,
                                "${permission}에 대한 권한이 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.i("TAG", "I can't work for you anymore then. ByeBye!")
                        } else {
                            //getMyLocation()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mMap.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMap.onStop()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            locationReceiver, IntentFilter(ACTION_LOCATION_UPDATE))
        val intent = Intent(activity, MyLocationService::class.java)
            .apply{ action = LOCATION_STOP }
        requireActivity().startService(intent)
    }

    override fun onResume() {
        super.onResume()
        mMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMap.onPause()
        timer?.cancel()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMap.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMap.onDestroy()

    }
}