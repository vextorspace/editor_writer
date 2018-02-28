package com.ronnev.editorselection.reports

import java.io.OutputStream

import com.itextpdf.text.{Document, Element, Font, PageSize, Phrase}
import com.itextpdf.text.pdf.{PdfPCell, PdfPTable, PdfWriter}
import com.ronnev.editorselection.assignment.GroupAssignment

import scala.util.Try

class PDFReport(val history: List[GroupAssignment]) {
    private val bigBoldFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD)
    private val boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)
    private val normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL)

    def write(outputStream: OutputStream) : Boolean = {
        val document = new Document(PageSize.LETTER, 20, 20, 20, 20)
        val result = Try({
            PdfWriter.getInstance(document, outputStream)
            document.open()

            history.foreach(groupAssignment => document.add(writeGroupAssignment(groupAssignment)))
        })
        if (document.isOpen) document.close()

        if (result.isFailure)
            println(s"failed to write pdf: ${result.get.asInstanceOf[Exception].getMessage}")

        return result.isSuccess
    }

    def writeGroupAssignment(groupAssignment: GroupAssignment): PdfPTable = {
        val table = new PdfPTable(2)

        val dateCell = new PdfPCell(new Phrase(groupAssignment.date.toString, bigBoldFont))
        dateCell.setColspan(2)

        table.addCell(dateCell)

        writeGroup("-- Group A --", groupAssignment.editorsPerWriterA(), groupAssignment.writersPerEditorB(), table)
        writeGroup("-- Group B --", groupAssignment.editorsPerWriterB(), groupAssignment.writersPerEditorA(), table)

        table
    }

    private def writeGroup(groupLabel: String, editorsPerWriter: Map[String, List[String]], writersPerEditor: Map[String, List[String]], table: PdfPTable) = {
        val groupLabelCell = new PdfPCell(new Phrase(groupLabel, boldFont))
        groupLabelCell.setHorizontalAlignment(Element.ALIGN_CENTER)
        groupLabelCell.setColspan(2)

        table.addCell(groupLabelCell)

        table.addCell(makeTableForGroupSection(writersPerEditor, "Editors:", "- Writers -", table))

        table.addCell(makeTableForGroupSection(editorsPerWriter, "Writers:", "- Editors -", table))
    }

    private def makeTableForGroupSection(personToPeople: Map[String, List[String]], label1: String, label2: String, table: PdfPTable): PdfPTable = {
        val numPeople = personToPeople.values.map(_.size).max
        val groupA1Table = new PdfPTable(numPeople + 1)

        val boldHeaderCell = new PdfPCell((new Phrase(label1, boldFont)))
        groupA1Table.addCell(boldHeaderCell)

        val headerA1Cell = new PdfPCell(new Phrase(label2, normalFont))
        headerA1Cell.setColspan(numPeople)
        headerA1Cell.setHorizontalAlignment(Element.ALIGN_CENTER)
        groupA1Table.addCell(headerA1Cell)

        personToPeople.foreach(row => addRow(groupA1Table, row._1, row._2, numPeople))

        groupA1Table
    }

    def addRow(table: PdfPTable, label: String, people: List[String], numPeople: Int) : Unit = {
        table.addCell(new Phrase(label, boldFont))
        0 until numPeople foreach ( index => {
            if (people.size > index)
                table.addCell(new Phrase(people(index), normalFont))
            else
                table.addCell(new Phrase("", normalFont))
        })
    }
}

object PDFReport {
    def apply(history: List[GroupAssignment]) = new PDFReport(history)
}