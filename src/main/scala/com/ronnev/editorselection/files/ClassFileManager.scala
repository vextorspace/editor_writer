package com.ronnev.editorselection.files

import collection.JavaConverters._
import com.ronnev.editorselection.{SchoolClass, StudentChangedListener}
import com.ronnev.editorselection.reports.PDFReport
import java.io.{File, FileOutputStream}
import javafx.scene.control.ButtonType



import scala.util.Try

class ClassFileManager(val fileSaver: FileSaver, val saveFileGetter: SaveFileGetter, val saveFirstAlerter: SaveFirstAlerter, val exportPDFFileGetter: ExportPDFFileGetter) extends StudentChangedListener {
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

    def exportHistory() : Boolean = {
        if (schoolClass == null)
            return false


        val exportFile : File =
        if (file == null || file.isDirectory)
            exportPDFFileGetter.getFile(new File(""))
        else {
            val dir = file.getParentFile
            val index = file.getName.lastIndexOf('.')
            val fileName : String =
            if (index >= 0)
                file.getName.substring(0, index)
            else
                file.getName

            exportPDFFileGetter.getFile(new File(dir, s"$fileName.pdf"))
        }

        val outputStream = new FileOutputStream(exportFile)
        val result = Try({
            PDFReport(getSchoolClass().history.asScala.toList).write(outputStream)
        })
        outputStream.close()

        return result.isSuccess
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
