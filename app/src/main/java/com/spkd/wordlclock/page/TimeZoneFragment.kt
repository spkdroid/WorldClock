package com.spkd.wordlclock.page

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.spkd.wordlclock.R
import com.spkd.wordlclock.adapter.TimeZoneAdapter
import com.spkd.wordlclock.adapter.TimeZoneAdapter.RecyclerTouchListener
import com.spkd.wordlclock.viewmodel.TimeZoneViewModel
import kotlinx.android.synthetic.main.time_zone_fragment.*
import kotlinx.android.synthetic.main.time_zone_list.view.*

class TimeZoneFragment : Fragment(), TextWatcher, TimeZoneAdapter.ClickListener {

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
        timeZoneList.addOnItemTouchListener(RecyclerTouchListener(requireContext(), this))
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
        timeZoneList.adapter!!.notifyItemChanged(1)
    }

    override fun onClick(view: View, position: Int) {
        val text = view.timeZoneTitle.text.toString() + "/" + view.timeZoneCityName.text.toString()
        viewModel.addTimeZoneToDatabase(text).observe(this, {
            Toast.makeText(requireContext(), "Time Zone Added to the DashBoard", Toast.LENGTH_LONG)
                .show()
        })
    }
}