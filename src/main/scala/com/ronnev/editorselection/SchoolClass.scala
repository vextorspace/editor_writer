package com.ronnev.editorselection

import java.io.File
import java.util

import scala.beans.BeanProperty

class SchoolClass {
    @BeanProperty var students: java.util.List[Student] = new util.ArrayList[Student]()
    @BeanProperty var history: java.util.List[GroupAssignment] = new util.ArrayList[GroupAssignment]()

    def addStudent(name: String) : Unit = students.add(Student(name))

    def removeStudent(name: String) : Unit = students.removeIf(_.name == name)

    def makeNewAssignment() : GroupAssignment = {
        val group = GroupAssignment()
        // -- magic goes here

        group
    }

    def acceptGroupAssignment(groupAssignment: GroupAssignment) : Unit = {
        history.add(groupAssignment)
    }
}

object SchoolClass {
    def apply() : SchoolClass = new SchoolClass()

    def apply(rosterCSV: File) : SchoolClass = {
        val schoolClass = SchoolClass()
        // load in csv file
        // add each to this school class
        schoolClass
    }
}
