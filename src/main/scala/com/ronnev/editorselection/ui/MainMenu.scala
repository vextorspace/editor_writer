package com.ronnev.editorselection.ui

import collection.JavaConverters._
import com.ronnev.editorselection.files.ClassFileManager
import java.util.Optional
import javafx.application.Platform
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.control._

import com.ronnev.editorselection.dates.SimpleDate


class MainMenu(val fileManager: ClassFileManager, val studentsDisplay: StudentDisplay, val groupsDisplay: GroupsDisplay, val historyDisplay: HistoryDisplay, val propertiesDisplay: PropertiesDisplay) {
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
            result.ifPresent {
                fileManager.getSchoolClass().addStudent(_)
                studentsDisplay.addStudent(_)
            }
        })
        editMenu.getItems.add(addStudentItem)

        val addStudentsItem = new MenuItem("Add Students from Text File")
        addStudentsItem.setOnAction(event => {
            studentsDisplay.loadStudentsFromFile
        })
        editMenu.getItems.add(addStudentsItem)

        val removeStudentItem = new MenuItem("Remove Student")
        removeStudentItem.setOnAction(event => {
            studentRemover
            displayStudents
        })
        editMenu.getItems.add(removeStudentItem)

        val addExclusion = new MenuItem("Add Exclusion")
        addExclusion.setOnAction(event => {
            ExclusionDialog.showAndWait().ifPresent( exclude => {
                fileManager.getSchoolClass().addExclusion(exclude._1, exclude._2)
                groupsDisplay.addExclusion(exclude)
            })
        })
        editMenu.getItems.add(addExclusion)

        val removeExclusion = new MenuItem("Remove Exclusion")
        removeExclusion.setOnAction(event => {
            exclusionRemover
            displayStudents
        })
        editMenu.getItems.add(removeExclusion)


        val generateGroup = new MenuItem("Generate")
        generateGroup.setOnAction(event => {
            val dialog = new TextInputDialog("")
            dialog.setTitle("Date Entry")
            dialog.setHeaderText("Please enter date of group assignment")
            dialog.setContentText("Date YYYY-MM-DD:")

            val result: Optional[String] = dialog.showAndWait()
            result.ifPresent { dateString => {
                val date = SimpleDate(dateString)
                date.foreach( date => {
                    val group = fileManager.getSchoolClass().makeNewAssignment(date)
                    fileManager.getSchoolClass().acceptGroupAssignment(group)
                    historyDisplay.addHistory(group)
                })
            }}

        })
        editMenu.getItems.add(generateGroup)

        editMenu
    }

    private def displayStudents = {

        studentsDisplay.displayStudents(fileManager.getSchoolClass().students.asScala.toList)

        groupsDisplay.displayGroupA(fileManager.getSchoolClass().groupA.asScala.toList)
        groupsDisplay.displayGroupB(fileManager.getSchoolClass().groupB.asScala.toList)

        groupsDisplay.displayExclusions(fileManager.getSchoolClass().exclusions.asScala.toList.map( pair => {
            (pair.get(0), pair.get(1))
        }))

        propertiesDisplay.displayProperties(fileManager.getSchoolClass())

        historyDisplay.displayHistory(fileManager.getSchoolClass().history.asScala.toList)
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

    private def exclusionRemover: Unit = {
        val list = new ListView[String]()
        val items: ObservableList[String] = FXCollections.observableArrayList()
        fileManager.getSchoolClass()
            .exclusions
            .forEach(pair =>
                items.add(s"${pair.get(0)} | ${pair.get(1)}")
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
            .ifPresent(result => {
                val exclusions = result.split('|')

                println(s"result split: $result removing ${exclusions(0)} and ${exclusions(1)}")
                fileManager.getSchoolClass()
                    .removeExclusion(exclusions(0).trim, exclusions(1).trim)
            })

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
    def apply(fileManager: ClassFileManager, studentDisplay: StudentDisplay, groupsDisplay: GroupsDisplay, historyView: HistoryView, propertiesDisplay: PropertiesDisplay) =
        new MainMenu(fileManager, studentDisplay, groupsDisplay, historyView,propertiesDisplay)
}