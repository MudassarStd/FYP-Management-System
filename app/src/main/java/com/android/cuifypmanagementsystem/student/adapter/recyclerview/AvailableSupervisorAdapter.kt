package com.android.cuifypmanagementsystem.student.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.databinding.RvItemAvailableSupervisorsBinding
import com.android.cuifypmanagementsystem.datamodels.Teacher

interface OnSupervisorClickListener {
    fun onRequestSupervisorClick(teacher: Teacher)
}

class AvailableSupervisorAdapter(
    var teachers: List<Teacher>,
    private val listener: OnSupervisorClickListener,
    private val groupId : String?
) : RecyclerView.Adapter<AvailableSupervisorAdapter.AvailableSupervisorViewHolder>() {

    private var filteredList: List<Teacher> = teachers

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableSupervisorViewHolder {

        val binding = RvItemAvailableSupervisorsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AvailableSupervisorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvailableSupervisorViewHolder, position: Int) {
        val teacher = filteredList[position]
        var requestedAlready = false
        teacher.groupRequests?.forEach {
            it.requests?.forEach {
                if (it == groupId) {
                    requestedAlready = true
                }
            }
        }
        holder.bind(teacher, requestedAlready)
    }

    override fun getItemCount() = filteredList.size

    fun updateTeachers(teachers: List<Teacher>) {
        this.teachers = teachers
        filteredList = teachers
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            teachers
        } else {
            teachers.filter {
                it.name.contains(query, ignoreCase = true) || it.department.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    inner class AvailableSupervisorViewHolder(private val binding: RvItemAvailableSupervisorsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(teacher: Teacher, requestedAlready : Boolean) {
            binding.tvSupervisorName.text = teacher.name
            binding.tvSupervisorDepartment.text = teacher.department

            if (requestedAlready) {
                binding.btnRequestSupervisor.visibility = ViewGroup.GONE
                binding.tvRequestedSupervisor.visibility = ViewGroup.VISIBLE
            }

            binding.btnRequestSupervisor.setOnClickListener {
                 listener.onRequestSupervisorClick(teacher)
            }
        }
    }
}
