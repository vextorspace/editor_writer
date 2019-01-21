package com.ronnev.editorselection

import collection.JavaConverters._
import com.ronnev.editorselection.assignment.{AssignmentStrategy, ComboRestrictionAssignmentStrategy, GroupAssignment, MinAssigmentMeasurement}
import java.io.File
import java.util
import java.util.Comparator

import com.ronnev.editorselection.assignment.restrictions.{AssignmentRestriction, BadPairRestriction, HistoryRestriction}
import com.ronnev.editorselection.dates.SimpleDate

import scala.beans.BeanProperty

class SchoolClass {
    @BeanProperty var students: java.util.List[String] = new util.ArrayList[String]()
    @BeanProperty var history: java.util.List[GroupAssignment] = new util.ArrayList[GroupAssignment]()
  @BeanProperty var editorsPerWriter: Int = 3
  @BeanProperty var writersPerEditor: Int = 3
  @BeanProperty var byEditorsPerWriter: Boolean = true
    @BeanProperty var groupA: java.util.List[String] = new util.ArrayList[String]()
    @BeanProperty var groupB: java.util.List[String] = new util.ArrayList[String]()
    @BeanProperty var exclusions: java.util.List[java.util.List[String]] = new util.ArrayList[util.List[String]]()

    def addStudent(name: String) : Unit = {
        if ( ! students.contains(name.trim))
            students.add(name.trim)
    }

    def addStudents(names: String*) : Unit = {
        names.foreach(addStudent(_))
    }

    def removeStudent(name: String) : Unit = {
        students.removeIf(_ == name)
        groupA.removeIf(_ == name)
        groupB.removeIf(_ == name)
    }

    def removeStudents(names: String*) : Unit = {
        names.foreach(removeStudent(_))
    }

    def addExclusion(student1: String, student2: String) : Unit = {
        if (students.stream().anyMatch(_.equals(student1)) && students.stream().anyMatch(_.equals(student2)))
            if (exclusions.stream().allMatch(_.asScala.toList.sorted != List(student1, student2).sorted))
                exclusions.add(util.Arrays.asList(student1, student2))
    }

    def removeExclusion(student1: String, student2: String) : Unit = {
        exclusions.removeIf(_.asScala.toList.sorted == List(student1, student2).sorted)
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

    def makeEmptyNewAssigmnent(date: SimpleDate) : GroupAssignment = {
        val group = GroupAssignment(date)
        group.groupA = new util.ArrayList[String](groupA)
        group.groupB = new util.ArrayList[String](groupB)

        group
    }

    def makeNewAssignment(date: SimpleDate) : GroupAssignment = {
        var group = makeEmptyNewAssigmnent(date)

        val restrictions: List[AssignmentRestriction] = exclusions.asScala.toList.map(exclusion => {BadPairRestriction(exclusion.get(1), exclusion.get(2))})
                                                                  .union(List(HistoryRestriction(history.asScala.toList)))
        val strategy = ComboRestrictionAssignmentStrategy(restrictions)

      if (byEditorsPerWriter)
        strategy.makeAssignments(group, editorsPerWriter)
      else
        strategy.makeAssignmentsReversed(group, writersPerEditor)
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
