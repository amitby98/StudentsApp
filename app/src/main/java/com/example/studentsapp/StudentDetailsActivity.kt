package com.example.studentsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.studentsapp.R.id.main
import com.example.studentsapp.model.Model

class StudentDetailsActivity : AppCompatActivity() {
    private val model = Model.shared
    private var studentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        studentId = intent.getStringExtra("student_id")
        loadStudentDetails()
        setupEditButton()
    }

    private fun loadStudentDetails() {
        studentId?.let { id ->
            model.getStudentById(id)?.let { student ->
                findViewById<TextView>(R.id.student_details_name_text_view).text = student.name
                findViewById<TextView>(R.id.student_details_id_text_view).text = student.id
                findViewById<ImageView>(R.id.student_details_image_view).setImageResource(R.drawable.student)
                findViewById<TextView>(R.id.student_details_check_status_text_view).text =
                    if (student.isChecked) "Checked" else "Unchecked"
            }
        }
    }

    private fun setupEditButton() {
        findViewById<Button>(R.id.student_details_edit_button).setOnClickListener {
            val intent = Intent(this, EditStudentActivity::class.java)
            intent.putExtra("student_id", studentId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadStudentDetails()
    }
}
