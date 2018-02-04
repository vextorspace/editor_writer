package com.ronnev.editorselection.files

import java.io.File

import com.ronnev.editorselection.SchoolClass

import scala.util.Try

trait FileSaver {
    def saveFile(schoolClass: SchoolClass, file: File) : Boolean

    def loadFile(file: File) : Try[SchoolClass]
}
