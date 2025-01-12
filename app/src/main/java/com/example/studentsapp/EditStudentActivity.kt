package com.example.studentsapp

import android.content.Intent
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
    private var originalStudentId: String? = null
    private var originalStudent: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        originalStudentId = intent.getStringExtra("student_id")
        // Set the original student ID in the model
        originalStudentId?.let { model.setOriginalStudentId(it) }

        val nameEditText: EditText = findViewById(R.id.edit_student_activity_name_edit_text)
        val idEditText: EditText = findViewById(R.id.edit_student_activity_id_edit_text)
        val saveButton: Button = findViewById(R.id.edit_student_activity_save_button)
        val cancelButton: Button = findViewById(R.id.edit_student_activity_cancel_button)
        val deleteButton: Button = findViewById(R.id.edit_student_activity_delete_button)
        val avatarImageView: ImageView = findViewById(R.id.edit_student_activity_avatar)

        originalStudentId?.let { id ->
            originalStudent = model.getStudentById(id)
            originalStudent?.let { student ->
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

            originalStudent?.let { student ->
                // Create updated student with new information
                val updatedStudent = Student(
                    name = newName,
                    id = newId,
                    avatarUrl = student.avatarUrl,
                    isChecked = student.isChecked
                )

                // Update the student in the model
                model.updateStudent(updatedStudent)

                // Navigate back to student details with updated student ID
                val intent = Intent(this, StudentDetailsActivity::class.java).apply {
                    putExtra("student_id", newId)
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                startActivity(intent)
                finish()
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }

        deleteButton.setOnClickListener {
            originalStudentId?.let { id ->
                model.deleteStudent(id)
                Toast.makeText(this, "Student deleted successfully", Toast.LENGTH_SHORT).show()

                // Return to main activity
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
                finish()
            }
        }
    }
}