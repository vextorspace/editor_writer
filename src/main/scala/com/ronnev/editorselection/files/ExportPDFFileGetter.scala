package com.ronnev.editorselection.files

import java.io.File

trait ExportPDFFileGetter {
    def getFile(oldFile: File) : File
}
