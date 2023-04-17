package kr.ac.kumoh.newrun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kumoh.newrun.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigationBar()
    }

    private fun initNavigationBar(){
        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeMenu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrameView, HomeFragment())
                        .commit()
                }
                R.id.recordMenu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrameView, RecordFragment())
                        .commit()
                }
                R.id.runMenu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrameView, RunFragment())
                        .commit()
                }
                R.id.crewMenu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrameView, CrewFragment())
                        .commit()
                }
                R.id.imageMenu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrameView, ImageFragment())
                        .commit()
                }
            }
            true
        }
        binding.navigationView.selectedItemId = R.id.homeMenu
    }
}