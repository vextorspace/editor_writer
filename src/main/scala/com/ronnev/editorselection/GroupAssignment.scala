package com.ronnev.editorselection

import java.text.SimpleDateFormat
import java.util

import scala.beans.BeanProperty

class GroupAssignment {
    @BeanProperty var groupA: java.util.Set[String] = new util.LinkedHashSet[String]()
    @BeanProperty var groupB: java.util.Set[String] = new util.LinkedHashSet[String]()
    @BeanProperty var assignmentsA: java.util.Map[String, java.util.LinkedHashSet[String]] = new util.HashMap[String, java.util.LinkedHashSet[String]]()
    @BeanProperty var assignmentsB: java.util.Map[String, java.util.LinkedHashSet[String]] = new util.HashMap[String, java.util.LinkedHashSet[String]]()
    @BeanProperty var date: SimpleDate = SimpleDate()

    def addWriterToEditor(editor: String, writer: String) : Boolean = {
        if (groupA.contains(editor) && groupB.contains(writer)) {
            addToAssignment(assignmentsA, editor, writer)
            return true
        }
        if (groupB.contains(editor) && groupA.contains(writer)) {
            addToAssignment(assignmentsB, editor, writer)
            return true
        }

        return false
    }

    private def addToAssignment(assignments: java.util.Map[String, java.util.LinkedHashSet[String]], editor: String, writer: String): Unit = {
        if (assignments.containsKey(editor)) {
            assignments.get(editor).add(writer)
        } else {
            val newSet = new util.LinkedHashSet[String]()
            newSet.add(writer)
            assignments.put(editor, newSet)
        }
    }

    def dateFromString(dateString: String) : Unit = {
        val newDate = SimpleDate(dateString)
        newDate.foreach(date = _)
    }
}

object GroupAssignment {
    def apply() = new GroupAssignment()

    def apply(dateString: String): GroupAssignment = {
        val groupAssignment = new GroupAssignment()
        groupAssignment.dateFromString(dateString)
        groupAssignment
    }
}