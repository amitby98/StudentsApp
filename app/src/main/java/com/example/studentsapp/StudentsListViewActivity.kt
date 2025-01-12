package com.example.studentsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.studentsapp.model.Model
import com.example.studentsapp.model.Student
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StudentsListViewActivity : AppCompatActivity() {
    private var students: MutableList<Student>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_students_list_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListView()
        setupAddButton()
    }

    private fun setupListView() {
        students = Model.shared.students
        val listView: ListView = findViewById(R.id.students_list_view)
        listView.adapter = StudentsAdapter()

        listView.setOnItemClickListener { _, _, position, _ ->
            students?.get(position)?.let { student ->
                val intent = Intent(this, StudentDetailsActivity::class.java)
                intent.putExtra("student_id", student.id)
                startActivity(intent)
            }
        }
    }

    private fun setupAddButton() {
        val addButton: FloatingActionButton = findViewById(R.id.add_student_fab)
        addButton.setOnClickListener {
            startActivity(Intent(this, AddStudentActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        setupListView()
    }

    inner class StudentsAdapter : BaseAdapter() {
        override fun getCount(): Int = students?.size ?: 0

        override fun getItem(position: Int): Any = students?.get(position) ?: Any()

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(parent?.context).inflate(
                R.layout.student_list_row,
                parent,
                false
            )

            val student = students?.get(position)

            view.apply {
                findViewById<TextView>(R.id.student_row_name_text_view)?.text = student?.name
                findViewById<TextView>(R.id.student_row_id_text_view)?.text = student?.id
                findViewById<ImageView>(R.id.student_row_image_view)?.setImageResource(R.drawable.student)

                findViewById<CheckBox>(R.id.student_row_check_box)?.apply {
                    isChecked = student?.isChecked ?: false
                    tag = position

                    setOnClickListener { checkboxView ->
                        (checkboxView.tag as? Int)?.let { tagPosition ->
                            students?.get(tagPosition)?.let { taggedStudent ->
                                taggedStudent.isChecked = (checkboxView as? CheckBox)?.isChecked ?: false
                                Model.shared.updateStudentCheckStatus(taggedStudent.id, taggedStudent.isChecked)
                            }
                        }
                    }
                }
            }

            return view
        }
    }
}