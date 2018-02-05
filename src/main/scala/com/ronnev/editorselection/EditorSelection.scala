package com.ronnev.editorselection

import javafx.application.{Application, Platform}
import javafx.scene.Scene
import javafx.scene.control._
import javafx.scene.layout.{HBox, VBox}
import javafx.scene.paint.Color
import javafx.stage.Stage

import com.ronnev.editorselection.files.{ActualFileSaver, ClassFileManager, DialogSaveFileGetter, DialogSaveFirstAlerter}
import com.ronnev.editorselection.ui.MainMenu

class EditorSelection extends Application with StudentDisplay {
    private var fileManager : ClassFileManager= null
    private val textArea = new TextArea("")

    def displayStudents(students: List[String]) : Unit = {
        textArea.setText(students.mkString(System.lineSeparator()))
    }

    override def start(primaryStage: Stage): Unit = {
        fileManager = new ClassFileManager(ActualFileSaver, new DialogSaveFileGetter(primaryStage), DialogSaveFirstAlerter)

        primaryStage.setTitle("Messages")
        val mainBox = new HBox()
        val scene = new Scene(mainBox, 400, 350)
        scene.setFill(Color.OLDLACE)

        mainBox
             .getChildren()
             .addAll(
                 MainMenu(fileManager, this).createMenu()
             )

        textArea.setEditable(false)

        mainBox.getChildren.add(textArea)

        primaryStage.setScene(scene)

        primaryStage.toBack()

        primaryStage.show()
    }

    def launchMe() : Unit = Application.launch()
}

object EditorSelection {
    def apply() : EditorSelection = new EditorSelection()

    def main(args: Array[String]) : Unit = {
        EditorSelection().launchMe()
    }
}