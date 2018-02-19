package com.ronnev.editorselection.assignment.restrictions

import com.ronnev.editorselection.assignment.GroupAssignment

class HistoryRestriction(val history: List[GroupAssignment], val minDifference: Int) extends AssignmentRestriction {
    override def goodCombo(writer: String, editorCombo: List[String]): Boolean = {
        history.map{_.editorListFor(writer)}
            .filterNot(editors => {
                editorCombo.diff(editors).size >= minDifference
            })
            .isEmpty
    }
}

object HistoryRestriction {
    def apply(history: List[GroupAssignment]) = new HistoryRestriction(history, 1)

    def apply(history: List[GroupAssignment], minDifference: Int) = new HistoryRestriction(history, minDifference)
}