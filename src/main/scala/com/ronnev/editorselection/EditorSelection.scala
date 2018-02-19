package com.ronnev.editorselection

import java.nio.file.Paths
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.{HBox, Priority, Region, VBox}
import javafx.scene.paint.Color
import javafx.stage.Stage

import com.ronnev.editorselection.files.{ActualFileSaver, ClassFileManager, DialogSaveFileGetter, DialogSaveFirstAlerter}
import com.ronnev.editorselection.ui.{GroupsView, HistoryView, MainMenu, StudentsView}

class EditorSelection extends Application {

    private var fileManager : ClassFileManager= null

    override def start(primaryStage: Stage): Unit = {
        fileManager = new ClassFileManager(ActualFileSaver, new DialogSaveFileGetter(primaryStage), DialogSaveFirstAlerter)

        primaryStage.setTitle("Messages")
        val sceneBox = new VBox()
        val scene = new Scene(sceneBox, 400, 350)

        scene.getStylesheets.add(getClass.getResource("/com/ronnev/editorselection/ui/history-style.css").toString)
        scene.setFill(Color.OLDLACE)

        val mainBox = new HBox()

        sceneBox.getChildren.add(mainBox)

        val groupsView = GroupsView()
        val studentsView = StudentsView()
        studentsView.studentsAddedListeners += fileManager
        studentsView.studentsAddedListeners += groupsView
        val historyView = HistoryView()

        val groupButtonBox = initGroupButtonBox(studentsView, groupsView)

        mainBox.getChildren.addAll(studentsView, groupButtonBox, groupsView, historyView)

        primaryStage.setScene(scene)

        sceneBox
            .getChildren()
            .add(0,
                MainMenu(fileManager, studentsView, groupsView, historyView).createMenu()
            )

        primaryStage.show()
    }

    private def initGroupButtonBox(studentsView: StudentsView, groupsView: GroupsView) : VBox = {
        val groupAButton = new Button("--- GroupA --->")
        groupAButton.setOnAction(event => {
            if (!studentsView.selectedStudent().isEmpty) {
                if (fileManager.getSchoolClass().addStudentToGroupA(studentsView.selectedStudent()))
                    groupsView.addGroupA(studentsView.selectedStudent())
            }
        })

        val groupBButton = new Button("--- GroupB --->")
        groupBButton.setOnAction(event => {
            if (!studentsView.selectedStudent().isEmpty) {
                if (fileManager.getSchoolClass().addStudentToGroupB(studentsView.selectedStudent()))
                    groupsView.addGroupB(studentsView.selectedStudent())
            }
        })

        val spacer1 = new Region()
        VBox.setVgrow(spacer1, Priority.ALWAYS)
        val spacer2 = new Region()
        VBox.setVgrow(spacer2, Priority.ALWAYS)

        val vBox = new VBox()
        vBox.getChildren.addAll(spacer1, groupAButton, groupBButton, spacer2)
        vBox
    }

    def launchMe() : Unit = Application.launch()
}

object EditorSelection {
    def apply() : EditorSelection = new EditorSelection()

    def main(args: Array[String]) : Unit = {
        EditorSelection().launchMe()
    }
}