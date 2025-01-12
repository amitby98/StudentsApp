package com.example.studentsapp.adapter

import com.example.studentsapp.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsapp.model.Student

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    private var students: List<Student> = ArrayList()
    var onItemClick: ((Student) -> Unit)? = null
    var onCheckChanged: ((Student, Boolean) -> Unit)? = null

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.student_row_name_text_view)
        val idTextView: TextView = itemView.findViewById(R.id.student_row_id_text_view)
        val checkbox: CheckBox = itemView.findViewById(R.id.student_row_check_box)
        val studentImage: ImageView = itemView.findViewById(R.id.student_row_image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_list_row, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]

        holder.nameTextView.text = student.name
        holder.idTextView.text = student.id
        holder.checkbox.isChecked = student.isChecked
        holder.studentImage.setImageResource(R.drawable.student)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(student)
        }

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            onCheckChanged?.invoke(student, isChecked)
        }
    }

    override fun getItemCount() = students.size

    fun updateStudents(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
    }
}
