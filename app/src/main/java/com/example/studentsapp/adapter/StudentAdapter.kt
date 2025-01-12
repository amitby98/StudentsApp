package com.example.studentsapp.adapter

import com.example.studentsapp.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsapp.model.Model
import com.example.studentsapp.model.Student

class StudentAdapter(private val students: List<Student>?) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    var listener: OnItemClickListener? = null

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.student_row_name_text_view)
        private val idTextView: TextView = itemView.findViewById(R.id.student_row_id_text_view)
        private val checkBox: CheckBox = itemView.findViewById(R.id.student_row_check_box)
        private val imageView: ImageView = itemView.findViewById(R.id.student_row_image_view)
        private var student: Student? = null

        init {
            itemView.setOnClickListener {
                student?.let {
                    listener?.onItemClick(it)
                }
            }

            checkBox.setOnClickListener { view ->
                (view.tag as? Int)?.let { tag ->
                    student?.isChecked = (view as? CheckBox)?.isChecked ?: false
                    student?.let { Model.shared.updateStudentCheckStatus(it.id, it.isChecked) }
                }
            }
        }

        fun bind(student: Student?, position: Int) {
            this.student = student
            nameTextView.text = student?.name
            idTextView.text = student?.id
            imageView.setImageResource(R.drawable.student)
            checkBox.apply {
                isChecked = student?.isChecked ?: false
                tag = position
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_list_row, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students?.get(position), position)
    }

    override fun getItemCount(): Int = students?.size ?: 0
}

interface OnItemClickListener {
    fun onItemClick(student: Student)
}
