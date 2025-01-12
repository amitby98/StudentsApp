package com.example.studentsapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.studentsapp.model.Model
import com.example.studentsapp.model.Student

class EditStudentActivity : AppCompatActivity() {
    private val model = Model.shared
    private var studentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        studentId = intent.getStringExtra("student_id")

        val nameEditText: EditText = findViewById(R.id.edit_student_activity_name_edit_text)
        val idEditText: EditText = findViewById(R.id.edit_student_activity_id_edit_text)
        val saveButton: Button = findViewById(R.id.edit_student_activity_save_button)
        val cancelButton: Button = findViewById(R.id.edit_student_activity_cancel_button)
        val deleteButton: Button = findViewById(R.id.edit_student_activity_delete_button)
        val avatarImageView: ImageView = findViewById(R.id.edit_student_activity_avatar)

        studentId?.let { id ->
            model.getStudentById(id)?.let { student ->
                nameEditText.setText(student.name)
                idEditText.setText(student.id)
                avatarImageView.setImageResource(R.drawable.student)
            }
        }

        saveButton.setOnClickListener {
            val newName = nameEditText.text.toString().trim()
            val newId = idEditText.text.toString().trim()

            if (newName.isEmpty() || newId.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            studentId?.let { oldId ->
                model.getStudentById(oldId)?.let { student ->
                    val updatedStudent = Student(
                        name = newName,
                        id = newId,
                        avatarUrl = student.avatarUrl,
                        isChecked = student.isChecked
                    )
                    model.updateStudent(updatedStudent)
                    Toast.makeText(this, "Student updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }

        deleteButton.setOnClickListener {
            studentId?.let { id ->
                model.deleteStudent(id)
                Toast.makeText(this, "Student deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}