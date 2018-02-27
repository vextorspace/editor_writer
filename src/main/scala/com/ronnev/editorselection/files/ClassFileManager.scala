package com.ronnev.editorselection.files

import java.io.File

import com.ronnev.editorselection.{SchoolClass, StudentChangedListener}
import javafx.scene.control.ButtonType

import scala.util.Try

class ClassFileManager(val fileSaver: FileSaver, val saveFileGetter: SaveFileGetter, val saveFirstAlerter: SaveFirstAlerter) extends StudentChangedListener {
    private var schoolClass: SchoolClass = SchoolClass()
    private var file: File = null
    private var saveNeeded: Boolean = false

    def needsSaving = saveNeeded

    def setNeedsSaving() : Unit = {
        saveNeeded = true
    }

    def getSchoolClass() : SchoolClass = schoolClass

    def loadSchoolClass() : Boolean = {
        if (saveNeeded) {
            val alertResult = saveFirstAlerter.saveFirst()

            if(alertResult eq ButtonType.NO) saveNeeded = false
            else if (savedAndSuccessful(alertResult)) saveNeeded = false
        }

        if (saveNeeded) return false

        val newFile = saveFileGetter.getLoadFile(file)
        if (newFile == null || newFile.isDirectory || !newFile.canWrite)
            return false

        file = newFile
        val newSchoolClass: Try[SchoolClass] = fileSaver.loadFile(file)

        if(newSchoolClass.isFailure)
            return false

        schoolClass = newSchoolClass.get
        true
    }

    def saveAsSchoolClass() : Boolean = {
        file = saveFileGetter.getSaveFile(file)

        if (file != null) {
            saveNeeded = false
            fileSaver.saveFile(schoolClass, file)
        } else
            false
    }

    def saveSchoolClass() : Boolean = {
        if (file == null || !file.canWrite || file.isDirectory) {
            file = saveFileGetter.getSaveFile()
        }

        saveNeeded = false
        fileSaver.saveFile(schoolClass, file)
    }

    def newClass() : Boolean = {
        if (saveNeeded) {
            val alertResult = saveFirstAlerter.saveFirst()

            if (alertResult eq ButtonType.NO) saveNeeded = false
            else if (savedAndSuccessful(alertResult)) saveNeeded = false
        }

        if (saveNeeded) return false

        schoolClass = SchoolClass()
        saveNeeded = true
        true
    }

    private def savedAndSuccessful(alertResult: ButtonType) : Boolean= {
        if(alertResult != ButtonType.YES)
            return false
        saveSchoolClass()
    }

    override def onAdded(student: String) : Unit = {
        schoolClass.addStudent(student)
        saveNeeded = true
    }

    override def onRemoved(student: String) : Unit = {
        schoolClass.removeStudent(student)
        saveNeeded = true
    }

}
