package com.ronnev.editorselection.ui

trait StudentDisplay {
    def displayStudents(students: List[String]) : Unit

    def addStudent(student: String) : Unit

    def clear() : Unit
}
