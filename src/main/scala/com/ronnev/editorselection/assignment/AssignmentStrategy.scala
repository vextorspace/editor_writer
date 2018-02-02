package com.ronnev.editorselection.assignment

trait AssignmentStrategy {
    def makeAssigments(history: java.util.List[GroupAssignment], unassignedGroup: GroupAssignment, writersPerEditor: Int) : GroupAssignment
}
