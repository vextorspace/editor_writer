package com.ronnev.editorselection.ui

import com.ronnev.editorselection.assignment.GroupAssignment

trait HistoryDisplay {
    def displayHistory(history: List[GroupAssignment]): Unit

    def addHistory(groupAssignment: GroupAssignment): Unit

    def clear() : Unit
}
