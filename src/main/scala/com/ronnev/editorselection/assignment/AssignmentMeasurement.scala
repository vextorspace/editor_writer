package com.ronnev.editorselection.assignment

trait AssignmentMeasurement {
    def measurement(history: java.util.List[GroupAssignment], assignment: GroupAssignment) : Double
}
