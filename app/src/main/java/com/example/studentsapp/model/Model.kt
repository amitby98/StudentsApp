package com.example.studentsapp.model

import kotlin.text.set

class Model private constructor() {

    val students: MutableList<Student> = ArrayList()

    companion object {
        val shared = Model()
    }

    init {
        for (i in 0..20) {
            val student = Student(
                name = "Name $i",
                id = "Student ID: $i",
                avatarUrl = "",
                isChecked = false
            )
            students.add(student)
        }
    }

    fun addStudent(student: Student) {
        students.add(student)
    }

    fun getAllStudents(): List<Student> = students

    fun getStudentById(id: String): Student? {
        return students.find { it.id == id }
    }

    fun updateStudent(updatedStudent: Student) {
        val index = students.indexOfFirst { it.id == originalStudentId }
        if (index != -1) {
            // Remove the old student with the old ID
            students.removeAt(index)
            // Add the updated student
            students.add(index, updatedStudent)
        }
    }

    fun deleteStudent(id: String) {
        students.removeAll { it.id == id }
    }

    fun updateStudentCheckStatus(id: String, isChecked: Boolean) {
        val student = getStudentById(id)
        student?.let {
            it.isChecked = isChecked
            updateStudent(it)
        }
    }

    // Add this to track the original student ID when updating
    private var originalStudentId: String? = null

    // New method to set the original student ID before updating
    fun setOriginalStudentId(id: String) {
        originalStudentId = id
    }
}






