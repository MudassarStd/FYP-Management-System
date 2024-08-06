package com.android.cuifypmanagementsystem.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.datamodels.Teacher

class selectTeachersforFYPheadSEC(
    private val context: Context,
    private var teachersList: List<Teacher>
) : RecyclerView.Adapter<selectTeachersforFYPheadSEC.selectTeacherViewHolder>() {

    private lateinit var mListner: OnTeacherTestListItemListener

    interface OnTeacherTestListItemListener {
        fun onItemClick(teacher: Teacher)
    }

    fun setOnTeacherTestListItemListener(listner: OnTeacherTestListItemListener) {
        mListner = listner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): selectTeacherViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.rv_item_select_teacher, parent, false)
        return selectTeacherViewHolder(view, mListner)
    }

    override fun onBindViewHolder(holder: selectTeacherViewHolder, position: Int) {
        val teacher = teachersList[position]
        holder.teachername.text = teacher.name
        holder.departmentName.text = teacher.department
    }

    override fun getItemCount(): Int {
        return teachersList.size
    }

    fun updateTeachersList(teachersList: List<Teacher>) {
        this.teachersList = teachersList
        notifyDataSetChanged()
        Log.d("DisplayTeacherDebuggerAttached", "Adapter Update Func Data: ${teachersList} ")

    }


    inner class selectTeacherViewHolder(
        itemView: View,
        listner: selectTeachersforFYPheadSEC.OnTeacherTestListItemListener
    ) : RecyclerView.ViewHolder(itemView) {
        val teachername: TextView = itemView.findViewById(R.id.TVTeacherName)
        val departmentName: TextView = itemView.findViewById(R.id.TVdepartmentname)

        init {
            itemView.setOnClickListener {
                listner.onItemClick(teachersList[adapterPosition])
            }
        }
    }
}

