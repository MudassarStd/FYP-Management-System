package com.android.cuifypmanagementsystem.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.admin.BatchDetailsActivity
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.datamodels.Batch


interface OnAction{
    fun onDeleted(batch : Batch)
    fun onBatchSelectionForActivity(batch : Batch)
}
class BatchAdapter(var batches : List<Batch>, val context : Context)  : RecyclerView.Adapter<BatchAdapter.BatchViewHolder>() {

    private lateinit var interfaceListener: OnAction

    fun setOnCategoryClickListenerInterface(listener: OnAction) {
        interfaceListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val view = inflate.inflate(R.layout.rv_item_batch, parent, false)
        return BatchViewHolder(view, interfaceListener)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        holder.name.text = batches[position].name
        holder.semester.text = batches[position].semester.toString()+"th"
        holder.registeredStudents.text = batches[position].registeredStudents.toString()
        holder.registeredGroups.text = batches[position].registeredGroups.toString()
        holder.activityStatus.text = if(batches[position].fypActivityStatus) "On going" else "Available"
    }

    override fun getItemCount() = batches.size

    fun updateBatches(batches: List<Batch>) {
        this.batches = batches
        Log.d("TestingBatchLogic", "Inside Adapter data")
        notifyDataSetChanged()
    }


    inner class BatchViewHolder(itemView: View, listener: OnAction) :
        RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.tvBatchName)
        val semester = itemView.findViewById<TextView>(R.id.tvSemesterValue)
        val registeredStudents = itemView.findViewById<TextView>(R.id.tvRegisteredStudentsValue)
        val registeredGroups = itemView.findViewById<TextView>(R.id.tvRegisteredGroupsValue)
        val activityStatus = itemView.findViewById<TextView>(R.id.tvFypActivityStatusValue)

        init {
//            itemView.setOnClickListener {
//                val intent = Intent(context, BatchDetailsActivity::class.java)
//                intent.putExtra("batchId", batches[adapterPosition].firestoreId)
//                context.startActivity(intent)
//            }

            itemView.setOnClickListener {
                listener.onBatchSelectionForActivity(batches[adapterPosition])
            }

            itemView.setOnLongClickListener {
                listener.onDeleted(batches[adapterPosition])
                true
            }
        }
    }
}