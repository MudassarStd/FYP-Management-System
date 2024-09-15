
package com.android.cuifypmanagementsystem.admin.adapters.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.R

interface OnDepartSelected {
    fun onSelect(selectedDepartments: List<String>)
}

class DepartFilterAdapter(
    private var departments: List<String>,
    private val context: Context,
    private val listener: OnDepartSelected
) : RecyclerView.Adapter<DepartFilterAdapter.DepartFilterViewHolder>() {

    private val selectedDepartments = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartFilterViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val view = inflate.inflate(R.layout.rv_depart_filter_item, parent, false)
        return DepartFilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: DepartFilterViewHolder, position: Int) {
        val department = departments[position]
        holder.name.text = department
        holder.cb.isChecked = selectedDepartments.contains(department)

        holder.cb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!selectedDepartments.contains(department)) {
                    selectedDepartments.add(department)
                }
            } else {
                selectedDepartments.remove(department)
            }
            listener.onSelect(selectedDepartments)
        }
    }

    override fun getItemCount() = departments.size

    inner class DepartFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvDepartNameFilterList)
        val cb: CheckBox = itemView.findViewById(R.id.cbFitlerDepartList)
    }
}
