package com.ronnev.editorselection.assignment
import java.util
import collection.JavaConverters._


object MinAssigmentMeasurement extends AssignmentMeasurement {
    override def measurement(history: util.List[GroupAssignment], assignment: GroupAssignment): Double = {
        if (history.isEmpty)
            return Double.MaxValue
        history.asScala.map(groupToGroupMinimum(_, assignment)).min
    }

    def groupToGroupMinimum(group1: GroupAssignment, group2: GroupAssignment) : Double = {
        val editorsPerWriterA1 = group1.editorsPerWriterA()
        val editorsPerWriterA2 = group2.editorsPerWriterA()
        val editorsPerWriterB1 = group1.editorsPerWriterB()
        val editorsPerWriterB2 = group2.editorsPerWriterB()

        val gainedEditorsA = findAddedPairs(editorsPerWriterA1, editorsPerWriterA2)
        val gainedEditorsB = findAddedPairs(editorsPerWriterB1, editorsPerWriterB2)

        Seq(minAddedEditors(gainedEditorsA),
            minAddedEditors(gainedEditorsB))
            .min
    }

    private def minAddedEditors(lostEditorsA: Map[String, List[String]]) = {
        if (lostEditorsA.isEmpty) {
            0
        } else {
            lostEditorsA.map(pair => pair._2.size).min
        }
    }

    def findAddedPairs(editorsPerWriter1: Map[String, List[String]], editorsPerWriter2: Map[String, List[String]]) : Map[String, List[String]] = {
        editorsPerWriter2.map { pair =>
            if (editorsPerWriter1.contains(pair._1))
                (pair._1, pair._2.filterNot(editorsPerWriter1(pair._1).contains(_)))
            else
                (pair._1, pair._2)
        }
    }
}
