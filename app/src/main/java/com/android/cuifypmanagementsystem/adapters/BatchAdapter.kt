package com.android.cuifypmanagementsystem.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.admin.AddEditBatchActivity
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_EDIT_BATCH

interface OnAction {
    fun onDeleted(batch: Batch)
    fun onBatchSelectionForActivity(batch: Batch)
}

class BatchAdapter(
    private var batches: List<Batch>,
    private val context: Context
) : RecyclerView.Adapter<BatchAdapter.BatchViewHolder>() {

    // **Update: Renamed variables for clarity**
    private var isEditMode: Boolean = false
    private var isSelectionMode: Boolean = false
    private lateinit var onActionListener: OnAction

    // **Update: Method name clarified**
    fun setOnActionListener(listener: OnAction) {
        onActionListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_batch, parent, false)
        return BatchViewHolder(view, onActionListener)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        val batch = batches[position]

        holder.bind(batch, isEditMode, isSelectionMode)
    }

    override fun getItemCount() = batches.size

    // **Update: Clearer method name**
    fun updateBatchList(batches: List<Batch>) {
        this.batches = batches
        Log.d("TestingBatchLogic", "Adapter data updated")
        notifyDataSetChanged()
    }

    // **Update: Toggle methods made concise**
    fun toggleEditMode() {
        isEditMode = !isEditMode
        notifyDataSetChanged()
    }

    fun toggleBatchSelectionMode() {
        isSelectionMode = !isSelectionMode
        notifyDataSetChanged()
    }

    inner class BatchViewHolder(
        itemView: View,
        private val listener: OnAction
    ) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.tvBatchName)
        private val semester: TextView = itemView.findViewById(R.id.tvSemesterValue)
        private val registeredStudents: TextView = itemView.findViewById(R.id.tvRegisteredStudentsValue)
        private val registeredGroups: TextView = itemView.findViewById(R.id.tvRegisteredGroupsValue)
        private val activityStatus: TextView = itemView.findViewById(R.id.tvFypActivityStatusValue)
        private val tvAlreadySelectedBatch: TextView = itemView.findViewById(R.id.tvAlreadySelectedBatch)
        private val tvAlreadySelectedBatchUneditable: TextView = itemView.findViewById(R.id.tvAlreadySelectedBatchUneditable)
        private val btnEditBatch: Button = itemView.findViewById(R.id.btnEditFypBatch)
        private val btnSelectBatchForActivity: Button = itemView.findViewById(R.id.btnSelectFypBatchForActivity)

        // **Update: Introduced a bind method for better organization**
        fun bind(batch: Batch, isEditMode: Boolean, isSelectionMode: Boolean) {
            name.text = batch.name
            semester.text = "${batch.semester}th"
            registeredStudents.text = batch.registeredStudents.toString()
            registeredGroups.text = batch.registeredGroups.toString()
            activityStatus.text = if (batch.fypActivityStatus) "Ongoing" else "Available"

            tvAlreadySelectedBatchUneditable.visibility = if (isEditMode && batch.fypActivityStatus) View.VISIBLE else View.GONE
            btnEditBatch.visibility = if (isEditMode && !batch.fypActivityStatus) View.VISIBLE else View.GONE
            tvAlreadySelectedBatch.visibility = if (isSelectionMode && batch.fypActivityStatus) View.VISIBLE else View.GONE
            btnSelectBatchForActivity.visibility = if (isSelectionMode && !batch.fypActivityStatus) View.VISIBLE else View.GONE

            btnSelectBatchForActivity.setOnClickListener { listener.onBatchSelectionForActivity(batch) }

            itemView.setOnLongClickListener {
                listener.onDeleted(batch)
                true
            }

            btnEditBatch.setOnClickListener {
                val intent = Intent(context, AddEditBatchActivity::class.java).apply {
                    action = ACTION_EDIT_BATCH
                    putExtra("batch", batch)
                }
                context.startActivity(intent)
            }
        }
    }
}
