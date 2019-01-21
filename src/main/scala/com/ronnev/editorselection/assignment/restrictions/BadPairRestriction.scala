package com.ronnev.editorselection.assignment.restrictions

class BadPairRestriction(val person1: String, val person2: String) extends AssignmentRestriction {
    override def goodCombo(writer: String, editorCombo: List[String]): Boolean = {
        checkOneWay(person1, person2, writer, editorCombo) && checkOneWay(person2, person1, writer, editorCombo)
    }

  override def goodComboReversed
    (editor: String, writerCombo: List[String]): Boolean = {
        checkOneWay(person1, person2, editor, writerCombo) && checkOneWay(person2, person1, editor, writerCombo)
    }

    private def checkOneWay(person1: String, person2: String, writer: String, editorCombo: List[String]) = {
        if (writer == person1 && editorCombo.contains(person2)) {
            false
        } else {
            true
        }
    }
}

object BadPairRestriction {
    def apply(person1: String, person2: String) = new BadPairRestriction(person1, person2)
}
