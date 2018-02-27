package com.ronnev.editorselection.ui

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.scene.control.{Label, Spinner, SpinnerValueFactory}
import javafx.scene.layout.GridPane

import com.ronnev.editorselection.SchoolClass
import com.ronnev.editorselection.files.ClassFileManager

class PropertiesView(val fileManager: ClassFileManager) extends GridPane with PropertiesDisplay {
    private val editorsPerWriterSpinner = new Spinner[Integer]()
    editorsPerWriterSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 4))
    editorsPerWriterSpinner.valueProperty().addListener(new ChangeListener[Integer] {
        override def changed(observable: ObservableValue[_ <: Integer], oldValue: Integer, newValue: Integer): Unit = {
            fileManager.getSchoolClass().setEditorsPerWriter(newValue)
        }
    })
    this.add(new Label("Properties"), 0, 0, 2, 1)
    this.add(new Label("Editors per Writer"), 0, 1)
    this.add(editorsPerWriterSpinner, 1, 1)

    override def displayProperties(schoolClass: SchoolClass): Unit = {
        editorsPerWriterSpinner.getValueFactory.setValue(schoolClass.editorsPerWriter)
    }
}

object PropertiesView {
    def apply(fileManager: ClassFileManager) = new PropertiesView(fileManager)
}