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

    def addStudent(name: String) : Unit = students.add(name)

    def removeStudent(name: String) : Unit = students.removeIf(_ == name)

    def makeNewAssignment(date: SimpleDate) : GroupAssignment = {
        val group = GroupAssignment(date)
        // -- magic goes here
        strategy.makeAssigments(group)
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
