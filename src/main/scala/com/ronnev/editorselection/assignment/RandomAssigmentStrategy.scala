package com.ronnev.editorselection.assignment

import collection.JavaConverters._

object RandomAssigmentStrategy extends AssignmentStrategy {
    override def makeAssigments(history: java.util.List[GroupAssignment], unassignedGroup: GroupAssignment, writersPerEditor: Int): GroupAssignment = {
        var editorsForA = unassignedGroup.groupB.asScala.flatMap(editor => (1 to writersPerEditor).map(num => editor)).toList
        var editorsForB = unassignedGroup.groupA.asScala.flatMap(editor => (1 to writersPerEditor).map(num => editor)).toList

        val newGroup = unassignedGroup.copy()

        unassignedGroup.groupA.forEach(writer => {
            val editor = randomEditor(editorsForA)
            newGroup.addWriterToEditor(editor, writer)
            editorsForA = editorsForA - editor
        })

        unassignedGroup.groupB.forEach(writer => {
            newGroup.addWriterToEditor(randomEditor(editorsForB), writer)
        })

        unassignedGroup
    }

    def randomEditor(editors: List[String]) : String = {
        val index = Math.round(Math.random()*(editors.size-1)+.5).toInt
        editors(index)
    }
}
