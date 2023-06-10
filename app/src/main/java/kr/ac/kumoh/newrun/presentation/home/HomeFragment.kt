package kr.ac.kumoh.newrun.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.data.model.RankResponse
import kr.ac.kumoh.newrun.data.repository.MyRecordService
import kr.ac.kumoh.newrun.data.repository.RankService
import kr.ac.kumoh.newrun.domain.data.RankData
import kr.ac.kumoh.newrun.domain.data.UserInfo


class HomeFragment : Fragment() {
    private lateinit var goalProgressBar: ProgressBar
    private lateinit var rankListView: ListView
    private lateinit var rankAdapter: RankAdapter
    private lateinit var goalDataTextView: TextView
    private lateinit var recodeOfWeekTextView: TextView
    private lateinit var velocityOfWeekTextView: TextView
    private lateinit var calorieOfWeekTextView: TextView
    var rankList = emptyList<RankResponse>()

    val rankData = ArrayList<RankData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        goalProgressBar = view.findViewById(R.id.goalProgressBar)
        rankListView = view.findViewById(R.id.rankListView)
        goalDataTextView = view.findViewById(R.id.goalDataTextView)
        goalDataTextView.text = UserInfo.goalDistance.toString()
        recodeOfWeekTextView = view.findViewById(R.id.recodeOfWeekTextView)
        velocityOfWeekTextView = view.findViewById(R.id.velocityOfWeekTextView)
        calorieOfWeekTextView = view.findViewById(R.id.calorieOfWeekTextView)
        CoroutineScope(Dispatchers.IO).launch {
            val result = MyRecordService().weekRecord(UserInfo.userEmail.toString())
            CoroutineScope(Dispatchers.Main).launch {
                recodeOfWeekTextView.text =
                    (Math.round(result.totalDistance * 100) / 100.0).toString()
                velocityOfWeekTextView.text = (Math.round(result.avgSpeed * 100) / 100.0).toString()
                calorieOfWeekTextView.text =
                    (Math.round((result.avgSpeed * 100 / 3.5 * 4.5) * 100) / 100.0).toString()
                setUpGoalProgressBar()
            }
        }
        setUpRankListView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    private fun setUpRankListView() {
        CoroutineScope(Dispatchers.IO).launch {
            rankList = RankService().getRank()
            rankList.forEachIndexed{ index, item ->
                rankData.add(RankData(index+1, item.nickName, ((Math.round(item.totalDistance * 100) / 100.0).toFloat())))
            }
            CoroutineScope(Dispatchers.Main).launch {
                rankAdapter = RankAdapter(requireActivity(), rankData)
                rankListView.adapter = rankAdapter
            }
        }

    }

    private fun setUpGoalProgressBar() {
        // Set the progress value (0-100)
        goalProgressBar.progress = Math.round(recodeOfWeekTextView.text.toString().toFloat()/goalDataTextView.text.toString().toFloat()*100)
    }

}