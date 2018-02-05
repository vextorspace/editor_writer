package com.ronnev.editorselection

import javafx.application.{Application, Platform}
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.Scene
import javafx.scene.control._
import javafx.scene.input.MouseButton
import javafx.scene.layout.{HBox, VBox}
import javafx.scene.paint.Color
import javafx.stage.Stage

import com.ronnev.editorselection.files.{ActualFileSaver, ClassFileManager, DialogSaveFileGetter, DialogSaveFirstAlerter}
import com.ronnev.editorselection.ui.MainMenu

class EditorSelection extends Application with StudentDisplay {
    private var fileManager : ClassFileManager= null
    private val studentsListView = new ListView[String]()
    private val studentsModel: ObservableList[String] = FXCollections.observableArrayList()
    private val groupAListView = new ListView[String]()
    private val groupAModel: ObservableList[String] = FXCollections.observableArrayList()
    private val groupBListView = new ListView[String]()
    private val groupBModel: ObservableList[String] = FXCollections.observableArrayList()

    override def start(primaryStage: Stage): Unit = {
        fileManager = new ClassFileManager(ActualFileSaver, new DialogSaveFileGetter(primaryStage), DialogSaveFirstAlerter)

        primaryStage.setTitle("Messages")
        val sceneBox = new VBox()
        val scene = new Scene(sceneBox, 400, 350)
        scene.setFill(Color.OLDLACE)

        sceneBox
             .getChildren()
             .addAll(
                 MainMenu(fileManager, this).createMenu()
             )

        studentsListView.setEditable(false)
        studentsListView.setItems(studentsModel)
        studentsListView.setOnMouseClicked(event => {

            val student = studentsListView.getSelectionModel.getSelectedItem
            if(event.getButton == MouseButton.PRIMARY) {
                if (fileManager.getSchoolClass().addStudentToGroupA(student)) {
                    groupAModel.add(student)
                    fileManager.setNeedsSaving()
                }
            } else {
                if (fileManager.getSchoolClass().addStudentToGroupB(student)) {
                    groupBModel.add(student)
                    fileManager.setNeedsSaving()
                }
            }
        })

        val mainBox = new HBox()

        sceneBox.getChildren.add(mainBox)

        mainBox.getChildren.add(studentsListView)

        val groupsBox = new VBox()
        mainBox.getChildren.add(groupsBox)

        groupAListView.setEditable(false)
        groupAListView.setItems(groupAModel)
        groupBListView.setEditable(false)
        groupBListView.setItems(groupBModel)

        groupsBox.getChildren.addAll(groupAListView, groupBListView)

        primaryStage.setScene(scene)

        primaryStage.toBack()

        primaryStage.show()
    }

    def launchMe() : Unit = Application.launch()

    def displayStudents(students: List[String]) : Unit = {
        studentsModel.clear()
        students.foreach(studentsModel.add(_))
    }

    override def displayGroupA(students: List[String]): Unit = {
        studentsModel.clear()
        students.foreach(groupAModel.add(_))
    }

    override def displayGroupB(students: List[String]): Unit = {
        studentsModel.clear()
        students.foreach(groupBModel.add(_))
    }
}

object EditorSelection {
    def apply() : EditorSelection = new EditorSelection()

    def main(args: Array[String]) : Unit = {
        EditorSelection().launchMe()
    }
}