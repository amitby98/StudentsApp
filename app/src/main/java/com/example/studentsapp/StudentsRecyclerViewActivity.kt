package com.example.studentsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsapp.model.Model
import com.example.studentsapp.model.Student
import com.google.android.material.floatingactionbutton.FloatingActionButton

interface OnItemClickListener {
    fun onItemClick(position: Int)
    fun onItemClick(student: Student?)
}

class StudentsRecyclerViewActivity : AppCompatActivity() {
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var recyclerView: RecyclerView
    private var students: MutableList<Student>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLayout()
        setupRecyclerView()
    }

    private fun setupLayout() {
        mainLayout = ConstraintLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            id = View.generateViewId()
        }

        recyclerView = RecyclerView(this).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            layoutManager = LinearLayoutManager(this@StudentsRecyclerViewActivity)
        }

        val addButton = FloatingActionButton(this).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                setMargins(16.dpToPx(), 16.dpToPx(), 16.dpToPx(), 16.dpToPx())
            }
            setImageResource(R.drawable.ic_add)
            setOnClickListener {
                startActivity(Intent(this@StudentsRecyclerViewActivity, AddStudentActivity::class.java))
            }
        }

        mainLayout.apply {
            addView(recyclerView)
            addView(addButton)
        }

        setContentView(mainLayout)
    }

    private fun setupRecyclerView() {
        students = Model.shared.students
        val adapter = StudentsRecyclerAdapter(students)

        adapter.listener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.d("TAG", "On click Activity listener on position $position")
            }

            override fun onItemClick(student: Student?) {
                Log.d("TAG", "On student clicked name: ${student?.name}")
                student?.let {
                    val intent = Intent(this@StudentsRecyclerViewActivity, StudentDetailsActivity::class.java)
                    intent.putExtra("student_id", it.id)
                    startActivity(intent)
                }
            }
        }

        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    class StudentViewHolder(
        itemView: View,
        listener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.student_row_name_text_view)
        private val idTextView: TextView = itemView.findViewById(R.id.student_row_id_text_view)
        private val checkBox: CheckBox = itemView.findViewById(R.id.student_row_check_box)
        private val imageView: ImageView = itemView.findViewById(R.id.student_row_image_view)
        private var student: Student? = null

        init {
            checkBox.setOnClickListener { view ->
                (view.tag as? Int)?.let { tag ->
                    student?.isChecked = (view as? CheckBox)?.isChecked ?: false
                    student?.let { Model.shared.updateStudentCheckStatus(it.id, it.isChecked) }
                }
            }

            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
                listener?.onItemClick(student)
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

    class StudentsRecyclerAdapter(private val students: List<Student>?) :
        RecyclerView.Adapter<StudentViewHolder>() {

        var listener: OnItemClickListener? = null

        override fun getItemCount(): Int = students?.size ?: 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.student_list_row, parent, false)
            return StudentViewHolder(view, listener)
        }

        override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
            holder.bind(students?.get(position), position)
        }
    }
}