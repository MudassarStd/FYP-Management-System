package com.android.cuifypmanagementsystem.adapters

import android.content.Context
import android.content.Intent
import android.telecom.Call.Details
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.BatchDetailsActivity
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.room.Batch


interface OnAction{
    fun onDeleted(batch : Batch)
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
        holder.semester.text = batches[position].semester.toString()
    }

    override fun getItemCount() = batches.size

    fun updateBatches(batches: List<Batch>) {
        this.batches = batches
        notifyDataSetChanged()
    }


    inner class BatchViewHolder(itemView: View, listener: OnAction) :
        RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.tvBatchName)
        val semester = itemView.findViewById<TextView>(R.id.tvBatchSemester)
        init {
            itemView.setOnClickListener {
                val intent = Intent(context, BatchDetailsActivity::class.java)
                intent.putExtra("batchId", batches[adapterPosition].id)
                context.startActivity(intent)
            }

            itemView.setOnLongClickListener {
                listener.onDeleted(batches[adapterPosition])
                true
            }
        }
    }
}