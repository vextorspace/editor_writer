package com.ronnev.editorselection.ui

import java.util

import collection.JavaConverters._
import com.ronnev.editorselection.assignment.GroupAssignment
import javafx.scene.control.Label
import javafx.scene.layout.{GridPane, StackPane, VBox}

class GroupAssignmentEditorsToWritersDisplay(val groupAssignment: GroupAssignment) extends VBox{

    getChildren.add(new Label(groupAssignment.date.toString))
    getChildren.add(new Label("-- Assignment A --"))
    getChildren.add(makeGroupPanel(groupAssignment.groupB, groupAssignment.assignmentsA))
    getChildren.add(new Label("-- Assignment B --"))
    getChildren.add(makeGroupPanel(groupAssignment.groupA, groupAssignment.assignmentsB))

    private def makeGroupPanel(group: java.util.List[String], assignment: java.util.Map[String, java.util.ArrayList[String]]): GridPane = {
        val groupTable = new GridPane()
        groupTable.getStyleClass.add("grid")

        groupTable.add(makeHeaderCell("Editors"), 0, 0)
        groupTable.add(makeHeaderCell("Writers"), 2, 0, assignment.values.asScala.toList.map(_.size()).max, 1)


        val editors: List[String] = assignment.keySet().asScala.toList
        val maxWritersPerEditor = editors.map(assignment.get(_).size()).max

        for (index <- (1 to editors.size))
            makeEditorWritersRow(assignment, editors, groupTable, index, maxWritersPerEditor)

        groupTable
    }

    private def makeEditorWritersRow(assignment: util.Map[String, util.ArrayList[String]], editors: List[String], groupTable: GridPane, index: Int, maxWritersPerEditor: Int) = {
        val editor = editors(index - 1)
        val writersForEditor = assignment.get(editor).asScala.toList.sorted

        if (!writersForEditor.isEmpty) {
            groupTable.add(makeEditorCell(s"$editor:", index), 0, index)

            for (iWriter <- (1 to maxWritersPerEditor))
                if (iWriter <= writersForEditor.size)
                    groupTable.add(makeCell(s"${writersForEditor(iWriter - 1)}  ", index), iWriter, index)
                else
                    groupTable.add(makeCell("", index), iWriter, index)
        }
    }

    private def makeEditorCell(text: String, index: Int) : StackPane = {
        val cell = new StackPane()

        cell.getChildren().add(new Label(text))
        if (index % 2 == 0)
            cell.getStyleClass().add("historylabelcell")
        else
            cell.getStyleClass().add("oddhistorylabelcell")

        cell
    }

    private def makeCell(text: String, index: Int) : StackPane = {
        val cell = new StackPane()

        cell.getChildren().add(new Label(text))
        if (index % 2 == 0)
            cell.getStyleClass().add("historycell")
        else
            cell.getStyleClass().add("oddhistorycell")

        cell
    }

    private def makeHeaderCell(text: String) : StackPane = {
        val cell = new StackPane()

        cell.getChildren().add(new Label(text))
        cell.getStyleClass().add("headercell")

        cell
    }
}

object GroupAssignmentEditorsToWritersDisplay {
    def apply(groupAssignment: GroupAssignment) : GroupAssignmentEditorsToWritersDisplay = new GroupAssignmentEditorsToWritersDisplay(groupAssignment)
}
