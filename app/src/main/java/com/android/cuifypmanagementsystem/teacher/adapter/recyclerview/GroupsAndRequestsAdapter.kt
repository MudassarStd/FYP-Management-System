package com.android.cuifypmanagementsystem.teacher.adapter.recyclerview

import android.renderscript.Element.DataType
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.databinding.RvItemFypIdeaBinding
import com.android.cuifypmanagementsystem.datamodels.FypIdea
import com.android.cuifypmanagementsystem.datamodels.GroupDisplayModel
import com.android.cuifypmanagementsystem.teacher.utils.GroupDataType
import com.android.cuifypmanagementsystem.utils.DateTime.longToDate

class GroupsAndRequestsAdapter(
    private var data : List<GroupDisplayModel>,
    private val type : GroupDataType
) :
    RecyclerView.Adapter<GroupsAndRequestsAdapter.GroupsAndRequestsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsAndRequestsViewHolder {
        val binding = RvItemFypIdeaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GroupsAndRequestsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupsAndRequestsViewHolder, position: Int) {
        val dataItem = data[position]
        holder.bind(dataItem)
    }

    override fun getItemCount(): Int = data.size

    fun updateData(data : List<GroupDisplayModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class GroupsAndRequestsViewHolder(private val binding: RvItemFypIdeaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataItem : GroupDisplayModel) {

        }
    } }