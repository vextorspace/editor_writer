package com.ronnev.editorselection

import java.nio.file.Paths
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.{HBox, Priority, Region, VBox}
import javafx.scene.paint.Color
import javafx.stage.Stage

import com.ronnev.editorselection.files.{ActualFileSaver, ClassFileManager, DialogSaveFileGetter, DialogSaveFirstAlerter}
import com.ronnev.editorselection.ui._

class EditorSelection extends Application {

    private var fileManager : ClassFileManager= null

    override def start(primaryStage: Stage): Unit = {
        fileManager = new ClassFileManager(ActualFileSaver, new DialogSaveFileGetter(primaryStage), DialogSaveFirstAlerter)

        val groupsView = GroupsView()

        val studentsView = StudentsView()
        studentsView.studentsAddedListeners += fileManager
        studentsView.studentsAddedListeners += groupsView

        val historyView = HistoryView()
        HBox.setHgrow(historyView, Priority.ALWAYS)

        val propertiesView = PropertiesView(fileManager)

        val groupButtonBox = initGroupButtonBox(studentsView, groupsView)

        val sceneBox: VBox = setupScene(primaryStage, propertiesView, groupsView, studentsView, historyView, groupButtonBox)

        setupMenu(groupsView, propertiesView, studentsView, historyView, sceneBox)

        primaryStage.show()
    }

    private def setupMenu(groupsView: GroupsView, propertiesDisplay: PropertiesDisplay, studentsView: StudentsView, historyView: HistoryView, sceneBox: VBox) = {
        sceneBox
            .getChildren()
            .add(0,
                MainMenu(fileManager, studentsView, groupsView, historyView, propertiesDisplay).createMenu()
            )
    }

    private def setupScene(primaryStage: Stage, propertiesView: PropertiesView, groupsView: GroupsView, studentsView: StudentsView, historyView: HistoryView, groupButtonBox: VBox) = {
        primaryStage.setTitle("Messages")
        val sceneBox = new VBox()
        val scene = new Scene(sceneBox, 400, 350)

        scene.getStylesheets.add(getClass.getResource("/com/ronnev/editorselection/ui/history-style.css").toString)
        scene.setFill(Color.OLDLACE)

        val mainBox = new HBox()

        sceneBox.getChildren.add(mainBox)

        val firstColumn = new VBox()
        firstColumn.getChildren.addAll(propertiesView, studentsView)
        VBox.setVgrow(propertiesView, Priority.NEVER)
        VBox.setVgrow(studentsView, Priority.ALWAYS)
        mainBox.getChildren.addAll(firstColumn, groupButtonBox, groupsView, historyView)

        primaryStage.setScene(scene)
        sceneBox
    }

    private def initGroupButtonBox(studentsView: StudentsView, groupsView: GroupsView) : VBox = {
        val groupAButton = new Button("--- GroupA --->")
        groupAButton.setOnAction(event => {
            studentsView.selectedStudents().forEach(student => {
                if (fileManager.getSchoolClass().addStudentToGroupA(student))
                    groupsView.addGroupA(student)
            })
        })

        val groupBButton = new Button("--- GroupB --->")
        groupBButton.setOnAction(event => {
            studentsView.selectedStudents().forEach(student => {
                if (fileManager.getSchoolClass().addStudentToGroupB(student))
                    groupsView.addGroupB(student)
            })
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