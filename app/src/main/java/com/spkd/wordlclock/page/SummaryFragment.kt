package com.spkd.wordlclock.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.spkd.wordlclock.R
import com.spkd.wordlclock.adapter.SummaryTimeZoneAdapter
import com.spkd.wordlclock.viewmodel.SummaryViewModel
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import kotlinx.android.synthetic.main.summary_fragment.*
import kotlinx.android.synthetic.main.summary_time_zone_row.view.*


class SummaryFragment : Fragment(), SummaryTimeZoneAdapter.ClickListener {

    companion object {
        fun newInstance() = SummaryFragment()
    }

    private lateinit var viewModel: SummaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.spkd.wordlclock.R.layout.summary_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SummaryViewModel::class.java)
        viewModel.updateTimeZonesFromDatabase()
        viewModel.selectedTimeZoneList.observe(viewLifecycleOwner, {
            if (it != null) {
                summaryTimeZoneList.adapter = SummaryTimeZoneAdapter(requireContext(), it)
                summaryTimeZoneList.layoutManager =
                    LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                summaryTimeZoneList.setHasFixedSize(true)
            }
        })

        summaryTimeZoneList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )

        addTimeZoneButton.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_summaryFragment_to_timeZoneFragment)
        }

        summaryTimeZoneList.addOnItemTouchListener(
            SummaryTimeZoneAdapter.RecyclerTouchListener(
                requireContext(), this
            )
        )
    }


    override fun onResume() {
        super.onResume()
        if (summaryTimeZoneList.adapter != null) {
            summaryTimeZoneList.adapter!!.notifyDataSetChanged()
        }
    }

    override fun onClick(view: View, position: Int) {

        val message = view.time.text.toString() + "\n" + view.date.text.toString()

        val timeZoneDialog = MaterialDialog.Builder(requireActivity())
            .setTitle(view.TimeZoneNameText.text.toString())
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(
                "Delete", android.R.drawable.ic_delete
            ) { dialogInterface, which ->
                viewModel.removeTimeZoneFromDatabase(view.TimeZoneNameText.text.toString())
                dialogInterface.dismiss()
            }
            .setNegativeButton(
                "Cancel", android.R.drawable.ic_secure
            ) { dialogInterface, _ -> dialogInterface.dismiss() }
            .build()
        timeZoneDialog.show()
    }

}