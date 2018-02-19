package com.ronnev.editorselection.ui

import java.util.Optional
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.control.{ButtonType, Dialog, Label, TextField}
import javafx.scene.layout.GridPane

class ExclusionDialog extends Dialog[(String, String)] {
    private var studentsEntered : (Boolean, Boolean) = (false, false)
    this.setTitle("Exclusion Entry")
    this.setHeaderText("Enter the students to exclude")
    this.getDialogPane.getButtonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)

    val grid = new GridPane()
    grid.setHgap(10)
    grid.setVgap(10)
    grid.setPadding(new Insets(20, 150, 10, 10))

    val studentAField = new TextField()
    studentAField.setPromptText("Student")

    val studentBField = new TextField()
    studentBField.setPromptText("Student")

    grid.add(new Label("Student"), 0, 0)
    grid.add(studentAField, 1, 0)
    grid.add(new Label("Student"), 0, 1)
    grid.add(studentBField, 1, 1)

    getDialogPane.lookupButton(ButtonType.OK)
                 .setDisable(true)

    studentAField.textProperty().addListener((observable, oldValue, newValue) => {
        studentsEntered = studentsEntered.copy(_1 = newValue.trim().isEmpty())

        getDialogPane.lookupButton(ButtonType.OK)
                     .setDisable( studentsEntered._1 && studentsEntered._2 )
    })

    studentBField.textProperty().addListener((observable, oldValue, newValue) => {
        studentsEntered = studentsEntered.copy(_2 = newValue.trim().isEmpty())
        getDialogPane.lookupButton(ButtonType.OK)
            .setDisable( studentsEntered._1 && studentsEntered._2 )
    })


    this.getDialogPane.setContent(grid)

    Platform.runLater(() => studentAField.requestFocus)

    this.setResultConverter(dialogButton => {
        if (dialogButton eq ButtonType.OK)
            (studentAField.getText, studentBField.getText)
        else
            null
    })
}

object ExclusionDialog {
    def showAndWait() : Optional[(String, String)] = {
        val dialog = new ExclusionDialog

        dialog.showAndWait()
    }
}
