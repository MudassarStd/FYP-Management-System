package com.android.cuifypmanagementsystem.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.room.datamodels.FypActivity

class FypActivityAdapter(private val context : Context, private var activitiesData : List<FypActivity>) : RecyclerView.Adapter<FypActivityAdapter.ActivityViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FypActivityAdapter.ActivityViewHolder {
       val view=LayoutInflater.from(context).inflate(R.layout.rv_item_activity,parent,false)
        return ActivityViewHolder(view)
    }


    override fun onBindViewHolder(holder: FypActivityAdapter.ActivityViewHolder, position: Int) {
        val activity=activitiesData[position]
        holder.fypheadname.text=activity.fypHead
        holder.fypsecname.text=activity.fypSec
        holder.startedDate.text= activity.registrationTimeStamp.toString()
        holder.year.text=activity.startYear
        holder.status.text = if (activity.fypSec === "0") {
            "On going"
        } else {
            "On going"
        }
    }

    override fun getItemCount(): Int = activitiesData.size


    fun updateActivitiesData(activitiesData: List<FypActivity>)
    {
        this.activitiesData = activitiesData
        notifyDataSetChanged()
    }

    class ActivityViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val fypheadname=itemView.findViewById<TextView>(R.id.TVfypheadName)
        val fypsecname=itemView.findViewById<TextView>(R.id.TVfypSecName)
        val startedDate=itemView.findViewById<TextView>(R.id.TV_startedDate)
        val year=itemView.findViewById<TextView>(R.id.TV_year)
        val status=itemView.findViewById<TextView>(R.id.TV_status)
    }
}