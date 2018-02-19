package com.ronnev.editorselection.assignment

import java.util

import com.ronnev.editorselection.dates.SimpleDate

import collection.JavaConverters._
import scala.beans.BeanProperty
import scala.collection.mutable


class GroupAssignment {
    @BeanProperty var groupA: java.util.List[String] = new util.ArrayList[String]()
    @BeanProperty var groupB: java.util.List[String] = new util.ArrayList[String]()
    @BeanProperty var assignmentsA: java.util.Map[String, java.util.ArrayList[String]] = new util.HashMap[String, java.util.ArrayList[String]]()
    @BeanProperty var assignmentsB: java.util.Map[String, java.util.ArrayList[String]] = new util.HashMap[String, java.util.ArrayList[String]]()
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

        false
    }

    def writersPerEditorA() : Map[String, List[String]] = {
        assignmentsA.asScala.toMap.map{case (editor, writers) => (editor, writers.asScala.toList)}
    }

    def writersPerEditorB() : Map[String, List[String]] = {
        assignmentsB.asScala.toMap.map{case (editor, writers) => (editor, writers.asScala.toList)}
    }

    def editorsPerWriterA() : Map[String, List[String]] = {
        editorsPerWriter(assignmentsB)
    }

    def editorsPerWriterB() : Map[String, List[String]] = {
        editorsPerWriter(assignmentsA)
    }

    def editorsPerWriter(assignments: java.util.Map[String, java.util.ArrayList[String]]) : Map[String, List[String]] = {
        val newKeys: List[String] = assignments.asScala.values.flatMap(_.asScala).toList
        val newMap = mutable.Map[String, List[String]]()

        newKeys.foreach(key => newMap(key) = editorsFor(assignments, key))

        newMap.toMap
    }

    def editorsFor(assignments: java.util.Map[String, java.util.ArrayList[String]], writer: String) : List[String] = {
        assignments.asScala.filter{_._2.contains(writer)}.map(_._1).toList
    }

    def editorListFor(writer: String) : List[String] = {
        if (editorsPerWriterA().contains(writer))
            editorsPerWriterA()(writer)
        else if (editorsPerWriterB().contains(writer))
            editorsPerWriterB()(writer)
        else
            List.empty
    }

    private def addToAssignment(assignments: java.util.Map[String, java.util.ArrayList[String]], editor: String, writer: String): Unit = {
        if (assignments.containsKey(editor)) {
            assignments.get(editor).add(writer)
        } else {
            val newList = new util.ArrayList[String]()
            newList.add(writer)
            assignments.put(editor, newList)
        }
    }

    def dateFromString(dateString: String) : Unit = {
        val newDate = SimpleDate(dateString)
        newDate.foreach(date = _)
    }

    def copy() : GroupAssignment = {
        val newGroup = GroupAssignment(date.copy())
        newGroup.groupA = new util.ArrayList[String](groupA)
        newGroup.groupB = new util.ArrayList[String](groupB)

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