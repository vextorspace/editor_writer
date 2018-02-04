package com.ronnev.editorselection.files

import java.io.File

trait SaveFileGetter {
    def getSaveFile(oldFile: File) : File

    def getSaveFile() : File = getSaveFile(new File(""))

    def getLoadFile(oldFile: File) : File
}
