package com.ronnev.editorselection.assignment.restrictions

trait AssignmentRestriction {
    def goodCombo(writer: String, editorCombo: List[String]) : Boolean
}
