package com.ronnev.editorselection.ui

trait GroupsDisplay {
    def displayGroupA(students: List[String]) : Unit

    def addGroupA(student: String) : Unit

    def displayGroupB(students: List[String]) : Unit

    def addGroupB(student: String) : Unit

    def displayExclusions(studentPairs: List[(String, String)]): Unit

    def addExclusion(studentPair: (String, String)): Unit

    def clear() : Unit
}
