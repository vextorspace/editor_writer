package com.ronnev.editorselection.files
import java.io.File
import javafx.stage.{FileChooser, Window}

class DialogSaveFileGetter(val ownerWindow: Window) extends SaveFileGetter {

    override def getSaveFile(oldFile: File): File = {
        val fileSaver: FileChooser = makeDialog(oldFile, "Select file to save class")
        fileSaver.showSaveDialog(ownerWindow)
    }

    private def makeDialog(oldFile: File, title: String) = {
        val fileDialog = new FileChooser()
        fileDialog.setTitle(title)
        if (oldFile != null) {
            fileDialog.setInitialDirectory(oldFile.getParentFile)
            fileDialog.setInitialFileName(oldFile.getName)
        }

        val extFilter = new FileChooser.ExtensionFilter("class files (*.clss)", "*.clss")
        fileDialog.getExtensionFilters().add(extFilter)

        fileDialog
    }

    override def getLoadFile(oldFile: File): File = {
        val fileLoader: FileChooser = makeDialog(oldFile, "Select file to load")
        fileLoader.showOpenDialog(ownerWindow)
    }
}
