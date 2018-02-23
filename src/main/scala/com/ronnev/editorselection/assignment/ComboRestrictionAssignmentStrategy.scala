package com.ronnev.editorselection.assignment

import collection.JavaConverters._
import com.ronnev.editorselection.assignment.restrictions.{AssignmentRestriction, HistoryRestriction}
import com.ronnev.math.CombinatorialOps._

class ComboRestrictionAssignmentStrategy(val restrictions: List[AssignmentRestriction]) extends AssignmentStrategy {

    override def makeAssignments(unassignedGroup: GroupAssignment, editorsPerWriter: Int): GroupAssignment = {
        val newGroup = unassignedGroup.copy()

        assignEditorsToWriters(newGroup.groupA.asScala.toList, newGroup.groupB.asScala.toList, newGroup, () => newGroup.writersPerEditorB(), editorsPerWriter)
        assignEditorsToWriters(newGroup.groupB.asScala.toList, newGroup.groupA.asScala.toList, newGroup, () => newGroup.writersPerEditorA(), editorsPerWriter)

        newGroup
    }

    private def assignEditorsToWriters(writers: List[String], editors: List[String], newGroup: GroupAssignment, editorsFunc : () => Map[String, List[String]], editorsPerWriter: Int) = {
        val editorCombos = editors.xcombinations(editorsPerWriter)

        writers.foreach(ComboRestrictionAssignmentStrategy.doOneWriter(_, editorCombos, editorsFunc, newGroup, restrictions))
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
        if (! restrictions.filter(_.isInstanceOf[HistoryRestriction]).isEmpty) {
            val historyRestriction = restrictions.filter(_.isInstanceOf[HistoryRestriction])(0).asInstanceOf[HistoryRestriction]

            if (!historyRestriction.history.isEmpty) {

                if (historyRestriction.history(0).editorsPerWriterA().contains(writer))
                    println(s"checking $writer + $editorCombo with restrictions A: ${historyRestriction.history(0).editorsPerWriterA()(writer)}")
                else if (historyRestriction.history(0).editorsPerWriterB().contains(writer))
                    println(s"checking $writer + $editorCombo with restrictions B: ${historyRestriction.history(0).editorsPerWriterB()(writer)}")
                else
                    println("huh?")

            }
        } else
            println("no history")
        restrictions.toStream.filterNot(restriction => restriction.goodCombo(writer, editorCombo)).isEmpty
    }

    def doOneWriter(writer: String, editorCombos: List[List[String]], editorsFunc : () => Map[String, List[String]], newGroup: GroupAssignment, restrictions: List[AssignmentRestriction]) : Unit = {
        editorCombos.sortWith(
            lessThanFunc(calculateLessers(editorsFunc)))
            .foreach(combo => println(s"combo: $combo"))
        editorCombos.sortWith(
            lessThanFunc(calculateLessers(editorsFunc)))
            .toStream
            .filter(checkCombo(writer, _, restrictions))
            .foreach(combo => println(s"combo checked: $combo"))

        editorCombos.sortWith(
                         lessThanFunc(calculateLessers(editorsFunc)))
                    .toStream
                    .filter(checkCombo(writer, _, restrictions))(0)
                    .foreach(editor => newGroup.addWriterToEditor(editor, writer))
    }

    private def calculateLessers(editorsFunc: () => Map[String, List[String]]) = {
        val writerNumbersPerEditor: Map[String, Int] = editorsFunc.apply().map { case (editor, writers) => (editor, writers.size) }
        if (writerNumbersPerEditor.isEmpty)
            writerNumbersPerEditor.keys.toList
        else {
            val lesser = writerNumbersPerEditor.values.min
            val lessers = writerNumbersPerEditor.keys.filter(writerNumbersPerEditor(_) == lesser).toList
            lessers.foreach(lesser => println(s"lesser: $lesser"))
            lessers
        }
    }

    def applyEditors(writer: String, editors: List[String], newGroup: GroupAssignment) : Unit = {
        editors.foreach(editor => newGroup.addWriterToEditor(editor, writer))
    }

    def lessThanFunc(lessers: List[String]) : (List[String], List[String]) => Boolean = {
        (list1: List[String], list2: List[String]) => lessers.diff(list1).size < lessers.diff(list2).size
    }
}