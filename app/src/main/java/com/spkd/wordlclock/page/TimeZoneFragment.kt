package com.spkd.wordlclock.page

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.spkd.wordlclock.R
import com.spkd.wordlclock.adapter.TimeZoneAdapter
import com.spkd.wordlclock.viewmodel.TimeZoneViewModel
import kotlinx.android.synthetic.main.time_zone_fragment.*

class TimeZoneFragment : Fragment(), TextWatcher {

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
            timeZoneList.adapter = TimeZoneAdapter(requireContext(), it)
        })
        timeZoneList.layoutManager = GridLayoutManager(context, 1)
        timeZoneList.setHasFixedSize(true)
        timeZoneList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
        timeZoneSearchInputText.addTextChangedListener(this)
    }

    override fun beforeTextChanged(textInput: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(textInput: CharSequence?, p1: Int, p2: Int, p3: Int) {
        viewModel.getCityList().observe(this, { it ->
            val temp = it.filter {
                it.contains(textInput.toString())
            }
            timeZoneList.adapter = TimeZoneAdapter(requireContext(), temp)
        })
    }

    override fun afterTextChanged(textInput: Editable?) {
        timeZoneList.adapter!!.notifyDataSetChanged()
    }

}