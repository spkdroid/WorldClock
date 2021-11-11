package com.spkd.wordlclock.adapter


import android.content.Context
import android.view.*
import android.widget.TextView
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
        holder.cityName.text = cityName
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

    class RadioViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        internal var cityName: TextView = itemView.findViewById(R.id.timeZoneTitle) as TextView
    }
}