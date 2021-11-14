package com.spkd.wordlclock.adapter

import android.content.Context
import android.text.format.Time
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.spkd.wordlclock.R
import java.util.*

class SummaryTimeZoneAdapter(
    private val mContext: Context,
    private val mData: List<com.spkd.worldclock.data.entity.TimeZone>
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<SummaryTimeZoneAdapter.SummaryViewHolder>() {

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(mContext)
        view = mInflater.inflate(R.layout.summary_time_zone_row, parent, false)
        return SummaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        val cityName = mData[position]
        holder.zoneName.text = cityName.uid

        val now = Time(cityName.uid)
        now.setToNow()
        holder.cityTime.text = now.format("%A %B %e")
        holder.cityDate.text = now.format("%H:%M")
        when (now.hour) {
            in 0..5 -> holder.zoneImage.setImageResource(R.drawable.midnight)
            in 7..11 -> holder.zoneImage.setImageResource(R.drawable.morning)
            in 12..15 -> holder.zoneImage.setImageResource(R.drawable.lunch)
            in 16..19 -> holder.zoneImage.setImageResource(R.drawable.evening)
            in 20..23 -> holder.zoneImage.setImageResource(R.drawable.night)
        }
    }


    interface ClickListener {
        fun onClick(view: View, position: Int)
    }

    internal class RecyclerTouchListener(
        context: Context,
        private val clicker: ClickListener?
    ) : androidx.recyclerview.widget.RecyclerView.OnItemTouchListener {
        private val gestureDetector: GestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }
            })

        override fun onInterceptTouchEvent(
            rv: androidx.recyclerview.widget.RecyclerView,
            e: MotionEvent
        ): Boolean {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clicker != null && gestureDetector.onTouchEvent(e)) {
                clicker.onClick(child, rv.getChildAdapterPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: androidx.recyclerview.widget.RecyclerView, e: MotionEvent) {

        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }
    }

    inner class SummaryViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        internal var zoneName: TextView = itemView.findViewById(R.id.TimeZoneNameText) as TextView
        internal var cityDate: TextView = itemView.findViewById(R.id.date) as TextView
        internal var cityTime: TextView = itemView.findViewById(R.id.time) as TextView
        internal var zoneImage: ImageView = itemView.findViewById(R.id.statusimage) as ImageView
    }


    // TODO: Replace the timezone with this function
    /*
    private fun getTime(timezone: String?): String? {
        val defaultTz: TimeZone = TimeZone.getDefault()
        TimeZone.setDefault(TimeZone.getTimeZone(timezone))
        val cal: Calendar = Calendar.getInstance()
        val date: Date = cal.getTime()
        val strDate: String = date.toString()
        // Reset Back to System Default
        TimeZone.setDefault(defaultTz)
        return strDate
    }
    */

}