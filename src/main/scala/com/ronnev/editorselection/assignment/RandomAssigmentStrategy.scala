package com.ronnev.editorselection.assignment

class RandomAssigmentStrategy extends AssignmentStrategy {
    override def makeAssigments(unassignedGroup: GroupAssignment): GroupAssignment = {

        unassignedGroup.copy()
    }
}
