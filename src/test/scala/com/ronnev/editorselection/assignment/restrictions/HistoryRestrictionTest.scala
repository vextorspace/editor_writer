package com.ronnev.editorselection.assignment.restrictions

import com.ronnev.editorselection.assignment.GroupAssignment
import org.scalatest.FeatureSpec

class HistoryRestrictionTest extends FeatureSpec {
    feature("history restriction with no history does not restrict") {
        scenario("no history") {
            val restriction = HistoryRestriction(List.empty[GroupAssignment])

            val result = restriction.goodCombo("fred", List("betty", "wilma"))

            assert(result == true)
        }
    }

    feature("history restriction with history only restricts if minimum difference not met") {
        scenario("history with same combo") {

            val assignment = GroupAssignment("2010-01-01")
            assignment.groupA.add("fred")
            assignment.groupA.add("barney")
            assignment.groupA.add("ted")

            assignment.groupB.add("wilma")
            assignment.groupB.add("betty")
            assignment.groupB.add("ed")

            assignment.addWriterToEditor("wilma", "fred")
            assignment.addWriterToEditor("betty", "fred")

            val history: List[GroupAssignment] = List(assignment)

            val restriction = HistoryRestriction(history, 1)

            val result = restriction.goodCombo("fred", List("wilma", "betty"))

            assert(result == false)

        }

        scenario("history with combo one different and 1 min difference") {

            val assignment = GroupAssignment("2010-01-01")
            assignment.groupA.add("fred")
            assignment.groupA.add("barney")
            assignment.groupA.add("ted")

            assignment.groupB.add("wilma")
            assignment.groupB.add("betty")
            assignment.groupB.add("ed")

            assignment.addWriterToEditor("wilma", "fred")
            assignment.addWriterToEditor("betty", "fred")

            val history: List[GroupAssignment] = List(assignment)

            val restriction = HistoryRestriction(history, 1)

            val result = restriction.goodCombo("fred", List("wilma", "ed"))

            assert(result == true)

        }

        scenario("history with combo one different and 2 min difference") {

            val assignment = GroupAssignment("2010-01-01")
            assignment.groupA.add("fred")
            assignment.groupA.add("barney")
            assignment.groupA.add("ted")

            assignment.groupB.add("wilma")
            assignment.groupB.add("betty")
            assignment.groupB.add("ed")

            assignment.addWriterToEditor("wilma", "fred")
            assignment.addWriterToEditor("betty", "fred")

            val history: List[GroupAssignment] = List(assignment)

            val restriction = HistoryRestriction(history, 2)

            val result = restriction.goodCombo("fred", List("wilma", "ed"))

            assert(result == false)

        }
    }
}
