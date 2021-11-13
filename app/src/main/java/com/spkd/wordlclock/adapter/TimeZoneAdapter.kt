package com.spkd.wordlclock.adapter


import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.spkd.wordlclock.R


/**
 * Adapter is the declaration of the recyclerview list item
 */
class TimeZoneAdapter(private val mContext: Context, private val mData: List<String>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<TimeZoneAdapter.RadioViewHolder>() {



    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(mContext)
        view = mInflater.inflate(R.layout.time_zone_list, parent, false)
        return RadioViewHolder(view)
    }

    override fun onBindViewHolder(holder: RadioViewHolder, position: Int) {
        val cityName = mData[position]

        if (cityName.indexOf("/") == -1) {
            holder.cityName.text = cityName
            holder.zoneName.text = ""
            holder.zoneImage.setImageResource(R.drawable.antarctica)
        } else {
            val name = cityName.split("/")
            holder.cityName.text = name[0]
            holder.zoneName.text = name[1]
            holder.zoneImage.setImageResource(R.drawable.america)
            when (holder.cityName.text) {
                "America", "Canada" -> holder.zoneImage.setImageResource(R.drawable.america)
                "Antarctica" -> holder.zoneImage.setImageResource(R.drawable.antarctica)
                "Asia" -> holder.zoneImage.setImageResource(R.drawable.asia)
                "Australia" -> holder.zoneImage.setImageResource(R.drawable.australia)
                "Europe" -> holder.zoneImage.setImageResource(R.drawable.europe)
                "Africa" -> holder.zoneImage.setImageResource(R.drawable.africa)
                else -> holder.zoneImage.setImageResource(R.drawable.america)
            }
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

    inner class RadioViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        internal var zoneName: TextView = itemView.findViewById(R.id.timeZoneTitle) as TextView
        internal var cityName: TextView = itemView.findViewById(R.id.timeZoneCityName) as TextView
        internal var zoneImage: ImageView = itemView.findViewById(R.id.iconImage) as ImageView
    }
}