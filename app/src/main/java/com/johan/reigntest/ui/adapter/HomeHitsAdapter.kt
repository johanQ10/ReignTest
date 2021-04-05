package com.johan.reigntest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.johan.reigntest.R
import com.johan.reigntest.model.Hit
import com.johan.reigntest.ui.HomeActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeHitsAdapter(private val act: HomeActivity, private val hits: ArrayList<Hit>?, private val homeClick: HomeClick):
    RecyclerView.Adapter<HomeHitsAdapter.ViewHolder>() {

    override fun getItemCount() = hits?.size ?: 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_hit, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = hits?.get(position)?.title ?: hits?.get(position)?.storyTitle
        holder.tvInfo.text = act.getString(R.string.hit_info, hits?.get(position)?.author, createdAtParsed(hits?.get(position)?.createdAt))

        holder.itemView.setOnClickListener { homeClick.hitClick(hits?.get(position)!!) }
    }

    fun pagination(hits: ArrayList<Hit>?) {
        val start = this.hits?.size
        this.hits?.addAll(hits!!)
        notifyItemRangeInserted(start!!, hits?.size!!)
    }

    fun getId(position: Int) = hits?.get(position)?.createdAtI

    fun delete(position: Int) {
        hits?.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvTitle: TextView = v.findViewById(R.id.tv_title)
        var tvInfo: TextView = v.findViewById(R.id.tv_info)
    }

    private fun createdAtParsed(createdAt: String?): String {
        if (createdAt == "" || createdAt == null)
            return ""

        val createdAtTimeZone = createdAt.toDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").formatTo("yyyy-MM-dd HH:mm")

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)

        val parts = createdAtTimeZone.split(" ")
        val date = parts[0].split("-")
        val time = parts[1].split(":")

        return when {
            year - date[0].toInt() == 1 -> "1 year"
            year - date[0].toInt() > 1 -> "${year - date[0].toInt()} years"
            month - date[1].toInt() == 1 -> "1 moth"
            month - date[1].toInt() > 1 -> "${month - date[1].toInt()} months"
            day - date[2].toInt() == 1 -> "yesterday"
            day - date[2].toInt() > 1 -> "${day - date[2].toInt()} days"
            hour - time[0].toInt() > 0 -> "${hour - time[0].toInt()}h"
            else -> "${min - time[1].toInt()}m"
        }
    }

    private fun String.toDate(dateFormat: String, timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        parser.timeZone = timeZone
        return parser.parse(this)!!
    }

    private fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(this)
    }
}