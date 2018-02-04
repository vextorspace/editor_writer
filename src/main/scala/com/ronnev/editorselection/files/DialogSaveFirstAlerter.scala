package com.ronnev.editorselection.files
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.{Alert, ButtonType}

object DialogSaveFirstAlerter extends SaveFirstAlerter {
    override def saveFirst(): ButtonType = {
        val alert = new Alert(AlertType.CONFIRMATION, "Save changes to current class?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL)
        alert.showAndWait

        alert.getResult
    }
}
