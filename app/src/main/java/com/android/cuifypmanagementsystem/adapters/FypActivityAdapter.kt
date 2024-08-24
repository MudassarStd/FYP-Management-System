package com.android.cuifypmanagementsystem.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.admin.activities.FypDetailsActivity
import com.android.cuifypmanagementsystem.datamodels.FypActivityRecord

class FypActivityAdapter(private val context : Context, private var activitiesData : List<FypActivityRecord>) : RecyclerView.Adapter<FypActivityAdapter.ActivityViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActivityViewHolder {
       val view=LayoutInflater.from(context).inflate(R.layout.rv_item_activity,parent,false)
        return ActivityViewHolder(view)
    }


    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity=activitiesData[position]
        holder.fypheadname.text=activity.fypHeadId
        holder.fypsecname.text=activity.fypSecId
        holder.startedDate.text= activity.registrationTimeStamp.toString()
        holder.year.text=activity.batchId
        holder.status.text = if (activity.status) {
            "On going"
        } else {
            "Paused"
        }
        Log.d("fsdlfjsaoifhsadofhisd", "isActive; ${activity.status}")

    }

    override fun getItemCount(): Int = activitiesData.size


    fun updateActivitiesData(activitiesData: List<FypActivityRecord>)
    {
        this.activitiesData = activitiesData
        notifyDataSetChanged()
    }

    inner class ActivityViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val fypheadname=itemView.findViewById<TextView>(R.id.TVfypheadName)
        val fypsecname=itemView.findViewById<TextView>(R.id.TVfypSecName)
        val startedDate=itemView.findViewById<TextView>(R.id.TV_startedDate)
        val year=itemView.findViewById<TextView>(R.id.TV_year)
        val status=itemView.findViewById<TextView>(R.id.TV_status)

        init {
            itemView.setOnClickListener {
                Toast.makeText(context, "Activity details", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, FypDetailsActivity::class.java )
                intent.putExtra("fypActivityRecord", activitiesData[adapterPosition])
                context.startActivity(intent)
            }
        }

    }
}