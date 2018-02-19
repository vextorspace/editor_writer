package com.ronnev.editorselection.assignment

import collection.JavaConverters._
import com.ronnev.math.CombinatorialOps._

import scala.util.Try

object RandomAssignmentStrategy extends AssignmentStrategy {

    override def makeAssignments(history: java.util.List[GroupAssignment], unassignedGroup: GroupAssignment, editorsPerWriter: Int): GroupAssignment = {

        val maxWritersPerEditorA = unassignedGroup.groupB.size()*editorsPerWriter/unassignedGroup.groupA.size()
        val maxWritersPerEditorB = unassignedGroup.groupA.size()*editorsPerWriter/unassignedGroup.groupB.size()
        val editorsForA: List[String] = unassignedGroup.groupB.asScala.toList
        val editorsForB: List[String] = unassignedGroup.groupA.asScala.toList


        var newGroupA = unassignedGroup.copy()
        val writersA = unassignedGroup.groupA.asScala.toList

        while(assignWriters(writersA, editorsForA, editorsPerWriter, maxWritersPerEditorA, newGroupA, () => {newGroupA.writersPerEditorB()}).isFailure) {
            newGroupA = unassignedGroup.copy()
        }

        var newGroupB = newGroupA.copy()
        val writersB = newGroupA.groupB.asScala.toList

        while(assignWriters(writersB, editorsForB, editorsPerWriter, maxWritersPerEditorB, newGroupB, () => {newGroupB.writersPerEditorA()}).isFailure) {
            newGroupB = newGroupA.copy()
        }

        newGroupB
    }

    def assignWriters(writers: List[String], editors: List[String], editorsPerWriter: Int, maxWritersPerEditor: Int, newGroup: GroupAssignment, f: () => Map[String, List[String]]) : Try[Unit] = {
        Try {
            var availableEditors = editors
            writers.foreach(writer => {
                if (!availableEditors.isEmpty) {
                    createRandomEditorList(availableEditors, editorsPerWriter)
                        .foreach { editor => newGroup.addWriterToEditor(editor, writer) }
                    availableEditors = availableEditors diff overloadedEditors(f.apply(), maxWritersPerEditor)
                } else {
                    println("no editors")
                }
            })
        }
    }

    def overloadedEditors(writersPerEditor: Map[String, List[String]], maxWritersPerEditor: Int) : List[String] = {
        writersPerEditor.filterNot(_._2.size < maxWritersPerEditor)
                        .map(_._1)
                        .toList


    }

    def createRandomEditorList(editorList: List[String], editorsPerWriter: Int) : List[String] = {
        val editors = editorList.xcombinations(editorsPerWriter)
        val index = Math.floor(Math.random() * editors.size).toInt

        println(s"editorList: $editorList || combos: $editors || index: $index")
        editors(index)
    }
}
