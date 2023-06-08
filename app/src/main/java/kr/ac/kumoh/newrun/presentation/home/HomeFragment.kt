package kr.ac.kumoh.newrun.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.domain.data.RankData


class HomeFragment : Fragment() {
    private lateinit var goalProgressBar: ProgressBar
    private lateinit var rankListView: ListView
    private lateinit var rankAdapter: RankAdapter

    val rankData = listOf(
        RankData(1, "존잘용구", 31.2),
        RankData(2, "중요한게 뭘줄 알아?", 30.1),
        RankData(3, "신근쫄", 29.8),
        RankData(4, "금오산 마동석", 25.8),
        RankData(5, "누룽지맛치킨", 24.0),
        RankData(6, "게섯거라", 22.7),
        RankData(7, "누네띠네", 19.9),
        RankData(8, "거 참 뛰기 좋은 날이네", 15.1),
        RankData(9, "구미시 이봉주", 12.3),
        RankData(10, "다나카", 11.1),
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        goalProgressBar = view.findViewById(R.id.goalProgressBar)
        rankListView = view.findViewById(R.id.rankListView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpData()
    }

    private fun setUpData(){
        setUpGoalProgressBar()
        setUpRankListView()
    }

    private fun setUpRankListView() {
        rankAdapter = RankAdapter(requireActivity(), rankData)
        rankListView.adapter = rankAdapter
    }

    private fun setUpGoalProgressBar() {
        // Set the progress value (0-100)
        goalProgressBar.progress = 30
    }

}