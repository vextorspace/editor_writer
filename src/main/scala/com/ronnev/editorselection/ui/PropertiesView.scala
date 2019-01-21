package com.ronnev.editorselection.ui

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.scene.control.{Label, Spinner, SpinnerValueFactory, RadioButton, ToggleGroup, Toggle}
import javafx.scene.layout.GridPane

import com.ronnev.editorselection.SchoolClass
import com.ronnev.editorselection.files.ClassFileManager

class PropertiesView(val fileManager: ClassFileManager) extends GridPane with PropertiesDisplay {

  private val toggleGroup = new ToggleGroup()

  private val editorsPerWriterRadio = new RadioButton()
  editorsPerWriterRadio.setText("Editors per Writer")
  editorsPerWriterRadio.setToggleGroup(toggleGroup)

  private val writersPerEditorRadio = new RadioButton()
  writersPerEditorRadio.setText("Writers per Editor")
  writersPerEditorRadio.setToggleGroup(toggleGroup)


    private val editorsPerWriterSpinner = new Spinner[Integer]()
    editorsPerWriterSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 4))
    editorsPerWriterSpinner.valueProperty().addListener(new ChangeListener[Integer] {
        override def changed(observable: ObservableValue[_ <: Integer], oldValue: Integer, newValue: Integer): Unit = {
            fileManager.getSchoolClass().setEditorsPerWriter(newValue)
        }
    })

    private val writersPerEditorSpinner = new Spinner[Integer]()
    writersPerEditorSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 4))
    writersPerEditorSpinner.valueProperty().addListener(new ChangeListener[Integer] {
        override def changed(observable: ObservableValue[_ <: Integer], oldValue: Integer, newValue: Integer): Unit = {
            fileManager.getSchoolClass().setWritersPerEditor(newValue)
        }
    })

    this.add(new Label("Properties"), 0, 0, 2, 1)
    this.add(editorsPerWriterRadio, 0, 1)
    this.add(editorsPerWriterSpinner, 1, 1)

    this.add(writersPerEditorRadio, 0, 2)
    this.add(writersPerEditorSpinner, 1, 2)

    override def displayProperties(schoolClass: SchoolClass): Unit = {
        editorsPerWriterSpinner.getValueFactory.setValue(schoolClass.editorsPerWriter)
    }

  toggleGroup.selectedToggleProperty().addListener(
    new ChangeListener[Toggle] {
      override def changed(observable: ObservableValue[_ <:Toggle], oldToggle: Toggle, newToggle: Toggle): Unit = {
        newToggle match {
          case `editorsPerWriterRadio` => {
            editorsPerWriterSpinner.setDisable(false)
            writersPerEditorSpinner.setDisable(true)
            fileManager.getSchoolClass().setByEditorsPerWriter(true)
          }
          case `writersPerEditorRadio` => {
            editorsPerWriterSpinner.setDisable(true)
            writersPerEditorSpinner.setDisable(false)
            fileManager.getSchoolClass().setByEditorsPerWriter(false)            
          }
        }
      }
    }
  )


  writersPerEditorRadio.setSelected(true)
  
}

object PropertiesView {
    def apply(fileManager: ClassFileManager) = new PropertiesView(fileManager)
}
