package com.ronnev.editorselection.assignment

trait AssignmentStrategy {
    def makeAssigments(unassignedGroup: GroupAssignment) : GroupAssignment
}
