package com.ronnev.editorselection.ui

import collection.JavaConverters._
import com.ronnev.editorselection.StudentDisplay
import com.ronnev.editorselection.files.ClassFileManager
import java.util.Optional
import javafx.application.Platform
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.control._


class MainMenu(val fileManager: ClassFileManager, val studentsDisplay: StudentDisplay) {
    def createMenu() : MenuBar = {
        val menuBar = new MenuBar()

        menuBar.getMenus.add(createFileMenu)

        menuBar.getMenus.add(createEditMenu)

        menuBar
    }

    private def createEditMenu: Menu = {
        val editMenu = new Menu("Edit")

        val addStudentItem = new MenuItem("Add Student")
        addStudentItem.setOnAction(event => {
            val dialog = new TextInputDialog("")
            dialog.setTitle("Add Student")
            dialog.setHeaderText("Please enter students name")
            dialog.setContentText("Name:")

            val result: Optional[String] = dialog.showAndWait()
            result.ifPresent(fileManager.getSchoolClass().addStudent(_))
            displayStudents
        })
        editMenu.getItems.add(addStudentItem)

        val removeStudentItem = new MenuItem("Remove Student")
        removeStudentItem.setOnAction(event => {
            studentRemover
            displayStudents
        })
        editMenu.getItems.add(removeStudentItem)

        editMenu
    }

    private def displayStudents = {
        studentsDisplay.displayStudents(fileManager.getSchoolClass().students.asScala.toList)
        studentsDisplay.displayGroupA(fileManager.getSchoolClass().groupA.asScala.toList)
        studentsDisplay.displayGroupB(fileManager.getSchoolClass().groupB.asScala.toList)
    }

    private def studentRemover: Unit = {
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

        dialog.setResultConverter( button => resultFromButton(button, list))

        dialog.showAndWait()
            .ifPresent(
                fileManager.getSchoolClass()
                    .removeStudent(_)
            )

    }

    def resultFromButton(buttonType: ButtonType, list: ListView[String]): String = {
        if (buttonType == ButtonType.OK)
            list.getSelectionModel.getSelectedItem().toString
        else
            ""
    }

    def createFileMenu = {
        val fileMenu = new Menu("File")

        val newMenuItem = new MenuItem("New")
        newMenuItem.setOnAction(event => {
            fileManager.newClass()
            displayStudents
        })
        fileMenu.getItems.add(newMenuItem)

        val loadMenuItem = new MenuItem("Load")
        loadMenuItem.setOnAction(event => {

            fileManager.loadSchoolClass()
            displayStudents
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
                Platform.exit()
                System.exit(0)
        })
        fileMenu.getItems.add(exitMenuItem)
        fileMenu
    }
}

object MainMenu {
    def apply(fileManager: ClassFileManager, studentDisplay: StudentDisplay) = new MainMenu(fileManager, studentDisplay)
}