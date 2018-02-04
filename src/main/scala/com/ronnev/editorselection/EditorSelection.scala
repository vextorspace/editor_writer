package com.ronnev.editorselection

import java.util.Optional
import javafx.application.{Application, Platform}
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.Scene
import javafx.scene.control.Alert.AlertType
import javafx.scene.control._
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage

import com.ronnev.editorselection.files.{ActualFileSaver, ClassFileManager, DialogSaveFileGetter, DialogSaveFirstAlerter}

class EditorSelection extends Application {
    private var fileManager : ClassFileManager= null

    override def start(primaryStage: Stage): Unit = {
        fileManager = new ClassFileManager(ActualFileSaver, new DialogSaveFileGetter(primaryStage), DialogSaveFirstAlerter)

        primaryStage.setTitle("Messages")
        val mainBox = new VBox()
        val scene = new Scene(mainBox, 400, 350)
        scene.setFill(Color.OLDLACE);

        val textArea = new TextArea("here")
        textArea.setEditable(false)

        val menuBar = createMenu()

        scene.getRoot().asInstanceOf[VBox].getChildren().addAll(menuBar)

        mainBox.getChildren.add(textArea)

        primaryStage.setScene(scene)

        primaryStage.toBack()

        primaryStage.show()
    }

    def launchMe() : Unit = Application.launch()

    def createMenu() : MenuBar = {
        val menuBar = new MenuBar()

        menuBar.getMenus.add(createFileMenu)

        val editMenu = new Menu("Edit")

        val addStudentItem = new MenuItem("Add Student")
        addStudentItem.setOnAction(event => {
            val dialog = new TextInputDialog("")
            dialog.setTitle("Add Student")
            dialog.setHeaderText("Please enter students name")
            dialog.setContentText("Name:")

            val result: Optional[String] = dialog.showAndWait()
            result.ifPresent(fileManager.getSchoolClass().addStudent(_))
        })
        editMenu.getItems.add(addStudentItem)

        val removeStudentItem = new MenuItem("Remove Student")
        removeStudentItem.setOnAction(event => {
            studentRemover match {
                case Some(toReturn) => return toReturn
                case None =>
            }

        })

        menuBar.getMenus.add(editMenu)

        menuBar
    }

    private def studentRemover = {
        val list = new ListView[String]()
        val items: ObservableList[String] = FXCollections.observableArrayList()
        fileManager.getSchoolClass()
            .students
            .forEach(student =>
                items.add(student)
            )

        list.setItems(items)

        list.setPrefWidth(200)
        list.setPrefHeight(100)

        val dialog: Dialog[String] = new Dialog()
        dialog.setTitle("Remove Student")
        dialog.setHeaderText("Select Student to Remove")

        dialog.getDialogPane.getButtonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)

        dialog.getDialogPane.setContent(list)

        dialog.setResultConverter(dialogButton => {
            if (dialogButton == ButtonType.OK)
                return list.getSelectionModel.getSelectedItem()

            ""
        }) : ( ButtonType => String)

        dialog.showAndWait()
            .ifPresent(
                fileManager.getSchoolClass()
                    .removeStudent(_)
            )
        None
    }

    def createFileMenu = {
        val fileMenu = new Menu("File")

        val newMenuItem = new MenuItem("New")
        newMenuItem.setOnAction(event => {
            fileManager.newClass()
        })
        fileMenu.getItems.add(newMenuItem)

        val loadMenuItem = new MenuItem("Load")
        loadMenuItem.setOnAction(event => {
            fileManager.loadSchoolClass()
        })
        fileMenu.getItems.add(loadMenuItem)

        val saveMenuItem = new MenuItem("Save")
        saveMenuItem.setOnAction(event => {
            fileManager.saveSchoolClass()
        })
        fileMenu.getItems.add(saveMenuItem)

        val saveAsMenuItem = new MenuItem("Save As")
        saveAsMenuItem.setOnAction(event => {
            fileManager.saveAsSchoolClass()
        })
        fileMenu.getItems.add(saveAsMenuItem)

        val exitMenuItem = new MenuItem("Exit")
        exitMenuItem.setOnAction(event => {
            if (fileManager.needsSaving && fileManager.saveSchoolClass())
                System.exit(0)
        })
        fileMenu.getItems.add(exitMenuItem)
        fileMenu
    }

}

object EditorSelection {
    def apply() : EditorSelection = new EditorSelection()

    def main(args: Array[String]) : Unit = {
        EditorSelection().launchMe()
    }
}