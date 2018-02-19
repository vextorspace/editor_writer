package com.ronnev.editorselection.assignment

trait AssignmentStrategy {
    def makeAssignments(history: java.util.List[GroupAssignment], unassignedGroup: GroupAssignment, editorsPerWriter: Int) : GroupAssignment
}
