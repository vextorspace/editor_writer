package com.ronnev.editorselection.assignment

trait AssignmentStrategy {
    def makeAssignments(unassignedGroup: GroupAssignment, editorsPerWriter: Int) : GroupAssignment
}
