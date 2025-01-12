package com.example.studentsapp.model

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
        val index = students.indexOfFirst { it.id == updatedStudent.id }
        if (index != -1) {
            students[index] = updatedStudent
        }
    }

    fun deleteStudent(id: String) {
        students.removeAll { it.id == id }
    }

    fun updateStudentCheckStatus(id: String, isChecked: Boolean) {
        getStudentById(id)?.let {
            it.isChecked = isChecked
            updateStudent(it)
        }
    }
}






