package com.spkd.wordlclock.page

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.spkd.wordlclock.R
import com.spkd.wordlclock.viewmodel.SummaryViewModel
import kotlinx.android.synthetic.main.summary_fragment.*

class SummaryFragment : Fragment() {

    companion object {
        fun newInstance() = SummaryFragment()
    }

    private lateinit var viewModel: SummaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.summary_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SummaryViewModel::class.java)
        // TODO: Use the ViewModel

        addTimeZoneButton.setOnClickListener {
              Navigation.findNavController(it).navigate(R.id.action_summaryFragment_to_timeZoneFragment)
        }


    }

}