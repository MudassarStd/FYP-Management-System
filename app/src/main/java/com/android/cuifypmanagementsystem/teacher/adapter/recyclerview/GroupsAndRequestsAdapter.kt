package com.android.cuifypmanagementsystem.teacher.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.databinding.RvItemGroupsOrRequestsBinding
import com.android.cuifypmanagementsystem.teacher.adapter.recyclerview.callbacks.OnGroupRequestClick
import com.android.cuifypmanagementsystem.teacher.datamodel.GroupDisplayModel
import com.android.cuifypmanagementsystem.teacher.utils.GroupDataType


class GroupsAndRequestsAdapter(
    private var data : List<GroupDisplayModel>,
    private val type : GroupDataType,
    private val listener : OnGroupRequestClick
) :
    RecyclerView.Adapter<GroupsAndRequestsAdapter.GroupsAndRequestsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsAndRequestsViewHolder {
        val binding = RvItemGroupsOrRequestsBinding.inflate(
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

    inner class GroupsAndRequestsViewHolder(private val binding: RvItemGroupsOrRequestsBinding) :
        RecyclerView.ViewHolder(binding.root) {

            init {
               setupButtons()
            }


        private fun setupButtons() {
            if (type == GroupDataType.GROUPS) {
                binding.btnViewMoreGroupDetail.visibility = View.VISIBLE
                binding.btnAddGroupToSupervision.visibility = View.GONE
            } else {
                binding.btnViewMoreGroupDetail.visibility = View.GONE
                binding.btnAddGroupToSupervision.visibility = View.VISIBLE

            }
        }

        fun bind(dataItem : GroupDisplayModel) {
            dataItem.project?.let { project ->
                binding.tvProjectTitleGroupList.text = project.title
                binding.tvProjectDescriptionGroupList.text = project.description
            }
            binding.tvGroupBatch.text = dataItem.batch

            binding.btnAddGroupToSupervision.setOnClickListener {
                listener.onApproveClick(dataItem.firestoreId!! , dataItem.batch)
                binding.btnAddGroupToSupervision.text = "Adding"
                binding.btnAddGroupToSupervision.isEnabled = false
            }
        }
    } }