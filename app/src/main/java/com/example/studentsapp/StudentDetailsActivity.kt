package com.example.studentsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.studentsapp.model.Model

class StudentDetailsActivity : AppCompatActivity() {
    private val model = Model.shared
    private var studentId: String? = null
    private lateinit var mainLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLayout()
        studentId = intent.getStringExtra("student_id")
        loadStudentDetails()
    }

    private fun setupLayout() {
        mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            setPadding(16.dpToPx(), 16.dpToPx(), 16.dpToPx(), 16.dpToPx())
            gravity = android.view.Gravity.CENTER_HORIZONTAL
        }

        val avatarImageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(120.dpToPx(), 120.dpToPx()).apply {
                topMargin = 24.dpToPx()
            }
            setImageResource(R.drawable.student)
        }

        val nameTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 16.dpToPx()
            }
            textSize = 24f
        }

        val idTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 8.dpToPx()
            }
            textSize = 18f
        }

        val statusTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 8.dpToPx()
            }
            textSize = 18f
        }

        val editButton = Button(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 24.dpToPx()
            }
            text = "Edit"
            setOnClickListener {
                val intent = Intent(this@StudentDetailsActivity, EditStudentActivity::class.java)
                intent.putExtra("student_id", studentId)
                startActivity(intent)
            }
        }

        mainLayout.apply {
            addView(avatarImageView)
            addView(nameTextView)
            addView(idTextView)
            addView(statusTextView)
            addView(editButton)
        }

        setContentView(mainLayout)
    }

    private fun loadStudentDetails() {
        studentId?.let { id ->
            model.getStudentById(id)?.let { student ->
                mainLayout.apply {
                    (getChildAt(1) as TextView).text = student.name
                    (getChildAt(2) as TextView).text = student.id
                    (getChildAt(3) as TextView).text = if (student.isChecked) "Checked" else "Unchecked"
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadStudentDetails()
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}
