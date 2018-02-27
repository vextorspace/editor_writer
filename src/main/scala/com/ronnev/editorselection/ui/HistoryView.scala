package com.ronnev.editorselection.ui

import javafx.scene.control.{Label, ScrollPane}
import javafx.scene.layout.GridPane

import com.ronnev.editorselection.assignment.GroupAssignment

class HistoryView extends ScrollPane with HistoryDisplay {

    private val grid = new GridPane()
    private var index: Int = 0

    setContent(grid)

    override def displayHistory(history: List[GroupAssignment]): Unit = {
        clear()

        history.foreach(addRow(_))
    }

    private def addRow(group: GroupAssignment): Unit = {
        println(s"adding group $index: ${group.editorsPerWriterA()}")
        grid.add(GroupAssignmentEditorsToWritersDisplay(group), 0, index)
        grid.add(GroupAssignmentWritersToEditorsDisplay(group), 1, index)
        index += 1

    }

    override def addHistory(groupAssignment: GroupAssignment): Unit = {
        addRow(groupAssignment)
    }

    override def clear(): Unit = {
        println("clear")
        grid.getChildren.clear()
        index = 0
    }
}

object HistoryView {
    def apply() = new HistoryView()
}