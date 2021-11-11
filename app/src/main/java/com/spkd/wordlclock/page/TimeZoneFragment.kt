package com.spkd.wordlclock.page

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.spkd.wordlclock.R
import com.spkd.wordlclock.adapter.TimeZoneAdapter
import com.spkd.wordlclock.viewmodel.TimeZoneViewModel
import kotlinx.android.synthetic.main.time_zone_fragment.*

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
        viewModel.getCityList().observe(requireActivity(), {
            timeZoneList.adapter = TimeZoneAdapter(requireContext(),it)
        })
        timeZoneList.layoutManager = GridLayoutManager(context, 1)
        timeZoneList.setHasFixedSize(true)
        timeZoneList.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.HORIZONTAL))
    }

}