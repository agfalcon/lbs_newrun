package kr.ac.kumoh.newrun

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar

class RecordFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var listView: ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_record, container, false)
        progressBar = view.findViewById(R.id.pgBar)
        listView = view.findViewById(R.id.listView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProgressBar()
        setupListView()
    }

    private fun setupProgressBar() {
        // Set the progress drawable color to yellow
        progressBar.progressDrawable.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN)

        // Set the progress value (0-100)
        progressBar.progress = 30
    }

    private fun setupListView() {
        val items = mutableListOf<String>()
        for (i in 1..10) {
            items.add("Item $i")
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter
    }
}