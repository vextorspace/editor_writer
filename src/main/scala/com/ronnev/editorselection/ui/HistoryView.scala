package com.ronnev.editorselection.ui

import javafx.scene.layout.VBox

import com.ronnev.editorselection.assignment.GroupAssignment

class HistoryView extends VBox with HistoryDisplay {


    override def displayHistory(history: List[GroupAssignment]): Unit = {
        getChildren.clear()
        history.foreach(group => getChildren.add(GroupAssignmentDisplay(group)))
    }

    override def addHistory(groupAssignment: GroupAssignment): Unit = getChildren.add(GroupAssignmentDisplay(groupAssignment))

    override def clear(): Unit = getChildren.clear()
}

object HistoryView {
    def apply() = new HistoryView()
}