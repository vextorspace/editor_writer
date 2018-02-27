package com.ronnev.editorselection.reports

import java.io.OutputStream
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import com.ronnev.editorselection.SchoolClass

import scala.util.Try

class PDFReport(val schoolClass: SchoolClass) {

    def write(outputStream: OutputStream) : Boolean = {
        val doc = new Document()
        Try({
            PDFWriter.getInstance(doc, outputStream)

            doc.open()
        })
        doc.close()


    }
}
