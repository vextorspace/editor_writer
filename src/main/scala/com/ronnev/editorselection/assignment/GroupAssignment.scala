package com.ronnev.editorselection.assignment

import java.util

import collection.JavaConverters._
import com.ronnev.editorselection.SimpleDate

import scala.beans.BeanProperty
import scala.collection.mutable


class GroupAssignment {
    @BeanProperty var groupA: java.util.Set[String] = new util.LinkedHashSet[String]()
    @BeanProperty var groupB: java.util.Set[String] = new util.LinkedHashSet[String]()
    @BeanProperty var assignmentsA: java.util.Map[String, java.util.LinkedHashSet[String]] = new util.HashMap[String, java.util.LinkedHashSet[String]]()
    @BeanProperty var assignmentsB: java.util.Map[String, java.util.LinkedHashSet[String]] = new util.HashMap[String, java.util.LinkedHashSet[String]]()
    @BeanProperty var date: SimpleDate = SimpleDate()

    def addWriterToEditor(editor: String, writer: String) : Boolean = {
        if (groupA.contains(editor) && groupB.contains(writer)) {
            addToAssignment(assignmentsB, editor, writer)
            return true
        }
        if (groupB.contains(editor) && groupA.contains(writer)) {
            addToAssignment(assignmentsA, editor, writer)
            return true
        }

        false
    }

    def editorsPerWriterA() : Map[String, Set[String]] = {
        editorsPerWriter(assignmentsA)
    }

    def editorsPerWriterB() : Map[String, Set[String]] = {
        editorsPerWriter(assignmentsB)
    }

    def editorsPerWriter(assignments: java.util.Map[String, java.util.LinkedHashSet[String]]) : Map[String, Set[String]] = {
        val newKeys: Set[String] = assignments.asScala.values.flatMap(_.asScala).toSet
        val newMap = mutable.Map[String, Set[String]]()

        newKeys.foreach(key => newMap(key) = editorsFor(assignments, key))

        newMap.toMap
    }

    def editorsFor(assignments: java.util.Map[String, java.util.LinkedHashSet[String]], writer: String) : Set[String] = {
        assignments.asScala.filter{_._2.contains(writer)}.map(_._1).toSet
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

    def copy() : GroupAssignment = {
        val newGroup = GroupAssignment(date.copy())
        newGroup.groupA = new util.HashSet[String](groupA)
        newGroup.groupB = new util.HashSet[String](groupB)

        newGroup
    }
}

object GroupAssignment {
    def apply() = new GroupAssignment()

    def apply(dateString: String): GroupAssignment = GroupAssignment.apply(SimpleDate(dateString).getOrElse(SimpleDate()))


    def apply(date: SimpleDate) : GroupAssignment = {

        val groupAssignment = new GroupAssignment
        groupAssignment.date = date

        groupAssignment
    }
}