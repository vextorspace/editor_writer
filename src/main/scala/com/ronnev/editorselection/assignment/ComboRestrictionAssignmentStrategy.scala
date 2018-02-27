package com.ronnev.editorselection.assignment

import collection.JavaConverters._
import com.ronnev.editorselection.assignment.restrictions.{AssignmentRestriction, HistoryRestriction}
import com.ronnev.math.CombinatorialOps._

class ComboRestrictionAssignmentStrategy(val restrictions: List[AssignmentRestriction]) extends AssignmentStrategy {

    override def makeAssignments(unassignedGroup: GroupAssignment, editorsPerWriter: Int): GroupAssignment = {
        val newGroup = unassignedGroup.copy()

        assignEditorsToWriters(newGroup.groupA.asScala.toList, newGroup.groupB.asScala.toList, newGroup, editorsPerWriter)
        assignEditorsToWriters(newGroup.groupB.asScala.toList, newGroup.groupA.asScala.toList, newGroup, editorsPerWriter)

        newGroup
    }

    private def assignEditorsToWriters(writers: List[String], editors: List[String], newGroup: GroupAssignment, editorsPerWriter: Int) = {
        val editorCombos = editors.xcombinations(editorsPerWriter)
        var editorsUsed = editors.map(editor => (editor, List.empty[String])).toMap
        writers.foreach(editorsUsed ++= ComboRestrictionAssignmentStrategy.doOneWriter(_, editorCombos, newGroup, restrictions, editorsUsed))
    }
}

object ComboRestrictionAssignmentStrategy {
    def apply(restrictions: List[AssignmentRestriction]) = new ComboRestrictionAssignmentStrategy(restrictions)

    def checkForLesser(editorCombo: List[String], lessers: List[String]) : Boolean = {
        if (editorCombo.size <= lessers.size)
            editorCombo.diff(lessers).isEmpty
        else
            lessers.diff(editorCombo).isEmpty
    }

    def checkCombo(writer: String, editorCombo: List[String], restrictions: List[AssignmentRestriction]) : Boolean = {
        restrictions.toStream.filterNot(restriction => restriction.goodCombo(writer, editorCombo)).isEmpty
    }

    def doOneWriter(writer: String,
                    editorCombos: List[List[String]],
                    newGroup: GroupAssignment,
                    restrictions: List[AssignmentRestriction],
                    editorsUsed : Map[String, List[String]]) :
    Map[String, List[String]] = {
        var editorsToWriters = editorsUsed

        editorCombos.sortWith(
                         lessThanFunc(calculateLessers(editorsUsed)))
                    .toStream
                    .filter(checkCombo(writer, _, restrictions))(0)
                    .foreach(editor => {
                        newGroup.addWriterToEditor(editor, writer)
                        editorsToWriters = updateEditorsToWriters(editorsToWriters, (editor, writer))
                    })
        editorsToWriters
    }
    def updateEditorsToWriters(map: Map[String, List[String]],  pair: (String, String)) : Map[String, List[String]] = {
        val map2 = Map(pair._1 -> List(pair._2))
        (map foldLeft map2) (
            (acc, v) => acc + (v._1 -> (v._2 ++ acc.getOrElse(v._1, List.empty[String])))
        )
    }

    private def calculateLessers(editorsUsed : Map[String, List[String]]) = {
        val writerNumbersPerEditor: Map[String, Int] = editorsUsed.map { case (editor, writers) => (editor, writers.size) }

        if (writerNumbersPerEditor.isEmpty)
            writerNumbersPerEditor.keys.toList
        else {
            val lesser = writerNumbersPerEditor.values.min
            writerNumbersPerEditor.keys.filter(writerNumbersPerEditor(_) == lesser).toList
        }
    }

    def applyEditors(writer: String, editors: List[String], newGroup: GroupAssignment) : Unit = {
        editors.foreach(editor => newGroup.addWriterToEditor(editor, writer))
    }

    def lessThanFunc(lessers: List[String]) : (List[String], List[String]) => Boolean = {
        (list1: List[String], list2: List[String]) => lessers.diff(list1).size < lessers.diff(list2).size
    }
}