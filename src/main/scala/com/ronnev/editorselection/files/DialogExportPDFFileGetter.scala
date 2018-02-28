package com.ronnev.editorselection.files

import java.io.File
import javafx.stage.{FileChooser, Window}

class DialogExportPDFFileGetter(val ownerWindow: Window) extends ExportPDFFileGetter {


    override def getFile(oldFile: File): File = {
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

        val extFilter = new FileChooser.ExtensionFilter("pdf files (*.pdf)", "*.pdf")
        fileDialog.getExtensionFilters().add(extFilter)

        fileDialog
    }
}

object DialogExportPDFFileGetter {
    def apply(ownerWindow: Window) = new DialogExportPDFFileGetter(ownerWindow)
}