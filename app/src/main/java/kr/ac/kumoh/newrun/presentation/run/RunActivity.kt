package kr.ac.kumoh.newrun.presentation.run

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kr.ac.kumoh.newrun.domain.data.ACTION_RUN_UPDATE
import kr.ac.kumoh.newrun.domain.data.DISTANCE_DATA_INFO
import kr.ac.kumoh.newrun.domain.data.LOCATION_STOP
import kr.ac.kumoh.newrun.domain.data.RUN_PAUSE
import kr.ac.kumoh.newrun.domain.data.RUN_START
import kr.ac.kumoh.newrun.domain.data.TIME_DATA_INFO
import kr.ac.kumoh.newrun.domain.data.TIME_RECORD
import kr.ac.kumoh.newrun.domain.model.TimeRecordData
import kr.ac.kumoh.newrun.domain.data.VELOCITY_DATA_INFO
import kr.ac.kumoh.newrun.databinding.ActivityRunBinding
import kr.ac.kumoh.newrun.service.MyLocationService

class RunActivity : AppCompatActivity() {

    private lateinit var timeRecord: Array<TimeRecordData>

    private val runDataReceiver = object : BroadcastReceiver() {
        @SuppressLint("NewApi")
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("테스트", "receive 완료")
            val time = intent.getIntExtra(TIME_DATA_INFO, -1)
            val distance = intent.getDoubleExtra(DISTANCE_DATA_INFO, -1.0)
            val velocity = intent.getDoubleExtra(VELOCITY_DATA_INFO, -1.0)
            timeRecord = intent.getParcelableArrayExtra(TIME_RECORD, TimeRecordData::class.java)!!
            if(time==-1 || distance == -1.0 || velocity == -1.0) return
            binding.kmValueTextView.text = distance.toString()
            binding.velocityValueTextView.text = String.format("%.2f",velocity)
            binding.timeValueTextView.text = String.format("%02d:%02d", time/60, time%60)
        }
    }

    private lateinit var binding: ActivityRunBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        binding.pauseButton.setOnClickListener {
            pauseRunning()
        }
    }

    override fun onResume() {
        Log.d("테스트", "receiver 등록")
        val filter = IntentFilter(ACTION_RUN_UPDATE)
        registerReceiver(runDataReceiver, filter)

        val intent = Intent(this, MyLocationService::class.java)
            .apply{ action = RUN_START }
        startService(intent)
        super.onResume()
    }

    override fun onPause() {
        unregisterReceiver(runDataReceiver)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, MyLocationService::class.java)
            .apply{ action = LOCATION_STOP }
        startService(intent)
    }

    private fun pauseRunning(){
        val intent = Intent(this, MyLocationService::class.java).apply{action = RUN_PAUSE }
        startService(intent)
        val runDataIntent = Intent(this, RunDataActivity::class.java).apply{
            val time = binding.timeValueTextView.text.toString().substring(0,2).toInt()*60 + binding.timeValueTextView.text.toString().substring(3,5).toInt()
            putExtra(TIME_DATA_INFO, time)
            putExtra(VELOCITY_DATA_INFO, binding.velocityValueTextView.text.toString().toDouble())
            putExtra(DISTANCE_DATA_INFO, binding.kmValueTextView.text.toString().toDouble())
            putExtra(TIME_RECORD, timeRecord)
        }
        startActivity(runDataIntent)
    }

    companion object{
        var activity : RunActivity? = null
    }
}