package com.android.cuifypmanagementsystem.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.admin.AddTeacher
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.room.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_EDIT_TEACHER_OBJECT
import com.android.cuifypmanagementsystem.utils.Constants.INTENT_ACTION_EDIT_TEACHER


interface OnTeacherEvents{
    fun onDeleteSignal(teacher: Teacher)
}
class TeacherAdapter(var teachers : List<Teacher>, val context : Context)  : RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder>() {

    private lateinit var interfaceListener: OnTeacherEvents

    fun setOnTeacherEventInterface(listener: OnTeacherEvents) {
        interfaceListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val view = inflate.inflate(R.layout.rv_item_teacher, parent, false)
        return TeacherViewHolder(view, interfaceListener)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        holder.name.text = teachers[position].name
        holder.id.text = teachers[position].id.toString()
        holder.depart.text = teachers[position].depart
    }

    override fun getItemCount() = teachers.size

    fun updateTeachers(teachers: List<Teacher>) {
        this.teachers = teachers
        notifyDataSetChanged()
    }

    inner class TeacherViewHolder(itemView: View, listener: OnTeacherEvents) :
        RecyclerView.ViewHolder(itemView) {
        val id = itemView.findViewById<TextView>(R.id.tvTeacherId)
        val name = itemView.findViewById<TextView>(R.id.tvTeacherName)
        val depart = itemView.findViewById<TextView>(R.id.tvTeacherDepart)
        val btnDelete = itemView.findViewById<ImageButton>(R.id.btnDeleteTeacher)
        val btnEdit = itemView.findViewById<ImageButton>(R.id.btnEditTeacher)
        init {
            btnDelete.setOnClickListener {
               listener.onDeleteSignal(teachers[adapterPosition])
            }

            btnEdit.setOnClickListener {
                val intent = Intent(context, AddTeacher::class.java).also{
                    it.action = INTENT_ACTION_EDIT_TEACHER
                    it.putExtra(ACTION_EDIT_TEACHER_OBJECT, teachers[adapterPosition])
                }
                context.startActivity(intent)
            }
        }
    }
}