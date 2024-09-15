package com.android.cuifypmanagementsystem.admin.adapters.recyclerview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.admin.activities.FypDetailsActivity
import com.android.cuifypmanagementsystem.datamodels.FypActivityRecordUiModel
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_OPEN_DETAILS_FOR_CLOSED_ACTIVITY
import com.android.cuifypmanagementsystem.utils.DateTime.longToDate


interface OnActivityAction {
    fun onFypActivitySelected()
}


class FypActivityAdapter(private val context : Context, private var activitiesData : List<FypActivityRecordUiModel>) : RecyclerView.Adapter<FypActivityAdapter.ActivityViewHolder>() {

    private lateinit var onActivityActionListener: OnActivityAction

    // **Update: Method name clarified**
    fun setOnActivityActionListener(listener: OnActivityAction) {
        onActivityActionListener = listener
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActivityViewHolder {
       val view=LayoutInflater.from(context).inflate(R.layout.rv_item_activity,parent,false)
        return ActivityViewHolder(view, onActivityActionListener)
    }


    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity=activitiesData[position]
        val date = longToDate(activity.fypActivityRecord!!.registrationTimeStamp)
        holder.fypheadname.text=activity.fypHeadName
        holder.fypsecname.text=activity.fypSecretoryName
        holder.startedDate.text= date
        holder.year.text=activity.batchName
        holder.status.text = if (activity.fypActivityRecord.status) {
            "On going"
        } else {
            "Closed"
        }
    }

    override fun getItemCount(): Int = activitiesData.size


    fun updateActivitiesData(activitiesData: List<FypActivityRecordUiModel>)
    {
        this.activitiesData = activitiesData
        notifyDataSetChanged()
    }

    inner class ActivityViewHolder(itemView: View, private val listener : OnActivityAction):RecyclerView.ViewHolder(itemView){
        val fypheadname=itemView.findViewById<TextView>(R.id.TVfypheadName)
        val fypsecname=itemView.findViewById<TextView>(R.id.TVfypSecName)
        val startedDate=itemView.findViewById<TextView>(R.id.TV_startedDate)
        val year=itemView.findViewById<TextView>(R.id.TV_year)
        val status=itemView.findViewById<TextView>(R.id.TV_status)

        init {
            itemView.setOnClickListener {
                Toast.makeText(context, "Activity details", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, FypDetailsActivity::class.java ).apply {
                    if(!activitiesData[adapterPosition].fypActivityRecord!!.status) {
                        action = ACTION_OPEN_DETAILS_FOR_CLOSED_ACTIVITY
                    }
                    putExtra("fypActivityRecord", activitiesData[adapterPosition].fypActivityRecord)
                }

                listener.onFypActivitySelected()
                context.startActivity(intent)
            }
        }
    }
}