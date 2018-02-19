package com.ronnev.editorselection.ui

import com.ronnev.editorselection.StudentChangedListener
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.control.{Button, ListView, TextField}
import javafx.scene.input.KeyCode
import javafx.scene.layout.{HBox, Priority, VBox}

import scala.collection.mutable

class StudentsView() extends VBox with StudentDisplay {
    val studentsAddedListeners = mutable.MutableList.empty[StudentChangedListener]

    private val studentsListView = new ListView[String]()
    private val newStudentTextField = new TextField()


    val studentsModel: ObservableList[String] = FXCollections.observableArrayList()

    studentsListView.setEditable(false)
    studentsListView.setItems(studentsModel)
    VBox.setVgrow(studentsListView, Priority.ALWAYS)

    getChildren.addAll(studentsListView, initNewStudentRow, initRemoveStudentRow)


    def selectedStudent() = studentsListView.getSelectionModel.getSelectedItem

    override def displayStudents(students: List[String]) : Unit = {
        studentsModel.clear()
        students.foreach(studentsModel.add(_))
    }

    override def addStudent(student: String) : Unit = studentsModel.add(student)

    override def clear(): Unit = studentsModel.clear()

    private def initNewStudentRow : HBox = {
        newStudentTextField.setPromptText("New Student")
        newStudentTextField.setOnKeyPressed(event => {
            if (event.getCode.equals(KeyCode.ENTER))
                newStudentAction
        })

        val hBox = new HBox()

        hBox.getChildren.add(newStudentTextField)

        val addButton = new Button("Add")


        addButton.setOnAction(event => newStudentAction)

        hBox.getChildren.add(addButton)

        hBox
    }

    private def newStudentAction = {
        if (!newStudentTextField.getText.trim.isEmpty) {
            studentsModel.add(newStudentTextField.getText)
            studentsAddedListeners.foreach(_.onAdded(newStudentTextField.getText))
            newStudentTextField.clear()
        }
    }

    private def initRemoveStudentRow : HBox = {
        val hBox = new HBox()
        val removeButton = new Button("Remove")
        removeButton.setMaxWidth(Double.MaxValue)

        removeButton.setOnAction(event => {
            if (!selectedStudent().isEmpty) {
                studentsAddedListeners.foreach(_.onRemoved(selectedStudent()))
                studentsModel.remove(selectedStudent())
            }
        })

        hBox.getChildren.add(removeButton)

        hBox
    }

}

object StudentsView {
    def apply(): StudentsView = new StudentsView()
}