package com.ronnev.editorselection.ui

import com.ronnev.editorselection.SchoolClass

trait PropertiesDisplay {
    def displayProperties(schoolClass: SchoolClass) : Unit
}
