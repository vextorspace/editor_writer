package com.ronnev.editorselection.ui

import com.ronnev.editorselection.StudentChangedListener
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.control._
import javafx.scene.input.KeyCode
import javafx.scene.layout.{HBox, Priority, VBox}
import javafx.stage.FileChooser

import scala.collection.mutable
import scala.io.Source

class StudentsView() extends VBox with StudentDisplay {
    val studentsAddedListeners = mutable.MutableList.empty[StudentChangedListener]

    private val studentsListView = new ListView[String]()
    private val newStudentTextField = new TextField()

    val studentsModel: ObservableList[String] = FXCollections.observableArrayList()

    studentsListView.setEditable(false)
    studentsListView.setItems(studentsModel)
    studentsListView.getSelectionModel.setSelectionMode(SelectionMode.MULTIPLE)
    VBox.setVgrow(studentsListView, Priority.ALWAYS)

    getChildren.addAll(studentsListView, initNewStudentRow, initRemoveStudentRow)


    def selectedStudents() = studentsListView.getSelectionModel.getSelectedItems

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

            selectedStudents().forEach(student => {
                studentsAddedListeners.foreach(_.onRemoved(student))
                studentsModel.remove(student)
            })
        })

        hBox.getChildren.add(removeButton)

        hBox
    }

    override def loadStudentsFromFile(): Unit = {
        val fileChooser = new FileChooser()
        fileChooser.setTitle("Open Students Text File")
        fileChooser.getExtensionFilters.addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"), new FileChooser.ExtensionFilter("All Files", "*.*"))

        val file = fileChooser.showOpenDialog(this.getScene.getWindow)

        if (file != null) {
            val bufferedSource = Source.fromFile(file)
            bufferedSource.getLines().foreach(student => addStudent(student))
            bufferedSource.close()
        }
    }
}

object StudentsView {
    def apply(): StudentsView = new StudentsView()
}