package com.ronnev.editorselection

import java.io.File
import java.util
import java.util.Comparator

import com.ronnev.editorselection.assignment.{AssignmentStrategy, GroupAssignment}

import scala.beans.BeanProperty

class SchoolClass {
    @BeanProperty var students: java.util.List[String] = new util.ArrayList[String]()
    @BeanProperty var history: java.util.List[GroupAssignment] = new util.ArrayList[GroupAssignment]()
    @BeanProperty var strategy: AssignmentStrategy = null
    @BeanProperty var maxWritersPerEditor: Int = 4
    @BeanProperty var groupA: java.util.List[String] = new util.ArrayList[String]()
    @BeanProperty var groupB: java.util.List[String] = new util.ArrayList[String]()

    def addStudent(name: String) : Unit = students.add(name)

    def removeStudent(name: String) : Unit = {
        students.removeIf(_ == name)
        groupA.removeIf(_ == name)
        groupB.removeIf(_ == name)
    }

    def addStudentToGroupA(name: String) : Boolean = {
        if (students.contains(name)) {
            if (!groupA.contains(name)) {
                groupA.add(name)
                return true
            }
        }

        false
    }

    def addStudentToGroupB(name: String) : Boolean = {
        if (students.contains(name)) {
            if (!groupB.contains(name)) {
                groupB.add(name)
                return true
            }
        }

        false
    }

    def makeNewAssignment(date: SimpleDate) : GroupAssignment = {
        val group = GroupAssignment(date)
        // -- magic goes here
        strategy.makeAssigments(history, group, maxWritersPerEditor)
    }

    def acceptGroupAssignment(groupAssignment: GroupAssignment) : Unit = {
        object GroupDateComparator extends Comparator[GroupAssignment] {
            override def compare(o1: GroupAssignment, o2: GroupAssignment): Int = o1.date.compareTo(o2.date)
        }

        history.removeIf(_.date == groupAssignment.date)
        history.add(groupAssignment)
        history.sort(GroupDateComparator)
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
