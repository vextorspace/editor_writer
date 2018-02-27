package com.ronnev.editorselection.ui

import java.util
import javafx.scene.control.Label
import javafx.scene.layout.{GridPane, StackPane, VBox}

import com.ronnev.editorselection.assignment.GroupAssignment

import scala.collection.JavaConverters._

class GroupAssignmentWritersToEditorsDisplay(val groupAssignment: GroupAssignment) extends VBox {

    getChildren.add(new Label(""))
    getChildren.add(new Label(""))
    getChildren.add(makeGroupPanel(groupAssignment.groupB, groupAssignment.editorsPerWriterB()))
    getChildren.add(new Label(""))
    getChildren.add(makeGroupPanel(groupAssignment.groupA, groupAssignment.editorsPerWriterA()))

    private def makeGroupPanel(group: java.util.List[String], editorsPerWriter: Map[String, List[String]]): GridPane = {

        val writers: List[String] = editorsPerWriter.keys.toList
        val maxEditorsPerWriter = writers.map(editorsPerWriter(_).size).max

        val groupTable = new GridPane()
        groupTable.getStyleClass.add("grid")

        groupTable.add(makeHeaderCell("Writers"), 0, 0)
        groupTable.add(makeHeaderCell("Editors"), 2, 0, maxEditorsPerWriter, 1)


        for (index <- (1 to writers.size))
            makeWritersPerEditorRow(editorsPerWriter, writers, groupTable, index, maxEditorsPerWriter)

        groupTable
    }

    private def makeWritersPerEditorRow(editorsPerWriter: Map[String, List[String]], writers: List[String], groupTable: GridPane, index: Int, maxWritersPerEditor: Int) = {
        val editor = writers(index - 1)
        val writersForEditor = editorsPerWriter(editor).sorted

        if (!writersForEditor.isEmpty) {
            groupTable.add(makeWriterCell(s"$editor:", index), 0, index)

            for (iWriter <- (1 to maxWritersPerEditor))
                if (iWriter <= writersForEditor.size)
                    groupTable.add(makeCell(s"${writersForEditor(iWriter - 1)}  ", index), iWriter, index)
                else
                    groupTable.add(makeCell("", index), iWriter, index)
        }
    }

    private def makeWriterCell(text: String, index: Int) : StackPane = {
        val cell = new StackPane()

        if (index % 2 == 0)
            cell.getStyleClass().add("historylabelcell")
        else
            cell.getStyleClass().add("oddhistorylabelcell")

        cell.getChildren().add(new Label(text))

        cell
    }

    private def makeCell(text: String, index: Int) : StackPane = {
        val cell = new StackPane()

        if (index % 2 == 0)
            cell.getStyleClass().add("historycell")
        else
            cell.getStyleClass().add("oddhistorycell")

        cell.getChildren().add(new Label(text))

        cell
    }

    private def makeHeaderCell(text: String) : StackPane = {
        val cell = new StackPane()
        cell.getStyleClass().add("headercell")
        cell.getChildren().add(new Label(text))

        cell
    }
}

object GroupAssignmentWritersToEditorsDisplay {
    def apply(groupAssignment: GroupAssignment) = new GroupAssignmentWritersToEditorsDisplay(groupAssignment)
}