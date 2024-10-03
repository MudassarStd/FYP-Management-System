package com.android.cuifypmanagementsystem.student.adapter.recyclerview


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.databinding.RvItemAvailableStudentBinding
import com.android.cuifypmanagementsystem.student.datamodel.Student

class AvailableStudentsAdapter(
    private var students: List<Student>,
    private val memberClickListener: OnMemberSelectionInterface // Interface listener
) : RecyclerView.Adapter<AvailableStudentsAdapter.AvailableStudentsViewHolder>() {

    interface OnMemberSelectionInterface {
        fun onMemberClicked(student: Student, isSelected: Boolean)
    }

    private val selectedStudents = mutableListOf<Student>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableStudentsViewHolder {
        val binding = RvItemAvailableStudentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AvailableStudentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvailableStudentsViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student, selectedStudents.contains(student))

        holder.binding.btnSelectMember.setOnClickListener {
            val isSelected = selectedStudents.contains(student)
            if (isSelected) {
                selectedStudents.remove(student)
            } else {
                if (selectedStudents.size < 2) {
                    selectedStudents.add(student)
                }
            }
            notifyItemChanged(position)
            // Notify the fragment via the interface about the selection
            memberClickListener.onMemberClicked(student, !isSelected)
        }
    }

    override fun getItemCount(): Int = students.size

    fun updateStudents(students: List<Student>) {
        this.students = students
        notifyDataSetChanged()
    }

    class AvailableStudentsViewHolder(val binding: RvItemAvailableStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student, isSelected: Boolean) {
            Log.d("AvailableStudentListFragmentTest", "Student being bound: $student")
            binding.tvStudentName.text = student.name
            binding.tvStudentRegisterationNumber.text = student.registrationNumber
            binding.btnSelectMember.text = if (isSelected) "Selected" else "Select"
            binding.textView12.text = adapterPosition.toString()
        }
    }
}




//class AvailableStudentsAdapter(private var students: List<Student>) :
//    RecyclerView.Adapter<AvailableStudentsAdapter.AvailableStudentsViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableStudentsViewHolder {
//        val binding = RvItemAvailableStudentBinding.inflate(
//            LayoutInflater.from(parent.context), parent, false
//        )
//        return AvailableStudentsViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: AvailableStudentsViewHolder, position: Int) {
//        val student = students[position]
//        holder.bind(student)
//    }
//
//    override fun getItemCount(): Int = students.size
//
//    fun updateIdeas(students: List<Student>) {
//        this.students = students
//        notifyDataSetChanged()
//        Log.d("AvailableStudentsFragmentDebuging", "Data in adapter: ${students}")
//    }
//
//    class AvailableStudentsViewHolder(private val binding: RvItemAvailableStudentBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(student: Student) {
//            binding.tvStudentName.text = student.name
//            binding.tvStudentRegisterationNumber.text = student.registrationNumber
//        }
//    }
//}
