package com.ronnev.editorselection.ui

trait StudentDisplay {
    def displayStudents(students: List[String]) : Unit

    def addStudent(student: String) : Unit

    def addStudents(students: List[String]) : Unit = {
        students.foreach(addStudent(_))
    }

    def loadStudentsFromFile() : Unit

    def clear() : Unit
}
