package com.spkd.wordlclock.page

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.spkd.wordlclock.R
import com.spkd.wordlclock.viewmodel.TimeZoneViewModel

class TimeZoneFragment : Fragment() {

    companion object {
        fun newInstance() = TimeZoneFragment()
    }

    private lateinit var viewModel: TimeZoneViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.time_zone_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TimeZoneViewModel::class.java)
        viewModel.getCityList().observe(requireActivity(), Observer {
            it.forEach { ram ->
                Toast.makeText(requireContext(),ram,Toast.LENGTH_LONG).show()
            }
        })

    }

}