package com.ronnev.editorselection.assignment
import java.util

import com.ronnev.editorselection.assignment.restrictions.AssignmentRestriction

class ComboRestrictionAssignmentStrategy(val restrictions: List[AssignmentRestriction]) extends AssignmentStrategy {

    override def makeAssignments(history: util.List[GroupAssignment], unassignedGroup: GroupAssignment, editorsPerWriter: Int): GroupAssignment = {

        unassignedGroup.copy()
    }
}
