package com.ronnev.editorselection

trait StudentChangedListener {
    def onAdded(student: String) : Unit

    def onRemoved(student: String) : Unit
}
