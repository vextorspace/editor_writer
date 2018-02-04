package com.ronnev.editorselection.assignment

import collection.JavaConverters._

object RandomAssigmentStrategy extends AssignmentStrategy {
    override def makeAssigments(history: java.util.List[GroupAssignment], unassignedGroup: GroupAssignment, writersPerEditor: Int): GroupAssignment = {
        var editorsForA = unassignedGroup.groupB.asScala.flatMap(editor => (1 to writersPerEditor).map(num => editor)).toList
        var editorsForB = unassignedGroup.groupA.asScala.flatMap(editor => (1 to writersPerEditor).map(num => editor)).toList

        val newGroup = unassignedGroup.copy()

        unassignedGroup.groupA.forEach(writer => {
            val editor = randomEditor(editorsForA)
            editorsForA = removeOneFromList(editorsForA, editor)

            newGroup.addWriterToEditor(editor, writer)
        })

        unassignedGroup.groupB.forEach(writer => {
            val editor = randomEditor(editorsForB)
            editorsForB = removeOneFromList(editorsForB, editor)

            newGroup.addWriterToEditor(editor, writer)
        })

        newGroup
    }

    def removeOneFromList(list: List[String], editor: String) : List[String] = {
        list diff List(editor)
    }

    def randomEditor(editors: List[String]) : String = {
        if(editors.isEmpty) {
            return ""
        }

        val index = Math.floor(Math.random()*editors.size).toInt
        editors(index)
    }
}
