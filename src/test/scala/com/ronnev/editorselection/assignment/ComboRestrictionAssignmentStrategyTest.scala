package com.ronnev.editorselection.assignment

import com.ronnev.editorselection.assignment.restrictions.{AssignmentRestriction, BadPairRestriction, HistoryRestriction}
import com.ronnev.editorselection.dates.SimpleDate
import org.scalatest.FeatureSpec

class ComboRestrictionAssignmentStrategyTest extends FeatureSpec {
    feature("ComboRestrictionAssignmentStrategy creates group assignments that do not violate restrictions") {
        scenario("An empty history restriction, no other restrictions, 3 by 3 with 2 per") {
            val restriction = HistoryRestriction(List.empty, 2)

            val strategy = ComboRestrictionAssignmentStrategy(List[AssignmentRestriction](restriction))

            val assignment = GroupAssignment("2010-01-01")
            assignment.groupA.add("fred")
            assignment.groupA.add("barney")
            assignment.groupA.add("ted")

            assignment.groupB.add("wilma")
            assignment.groupB.add("betty")
            assignment.groupB.add("ed")

            val result = strategy.makeAssignments(assignment, 2)
            assert(result.editorsPerWriterA().keys.size == 3)
            assert(result.editorsPerWriterB().keys.size == 3)
            assert(result.writersPerEditorA().keys.size == 3)
            assert(result.writersPerEditorB().keys.size == 3)
            assert(result.editorsPerWriterB().map(_._2.size).filterNot(_ == 2).isEmpty)
            assert(result.editorsPerWriterA().map(_._2.size).filterNot(_ == 2).isEmpty)
            assert(result.writersPerEditorA().map(_._2.size).filterNot(_ == 2).isEmpty)
            assert(result.writersPerEditorB().map(_._2.size).filterNot(_ == 2).isEmpty)
        }

        scenario("An empty history restriction, no other restrictions, 4 by 4 with 3 per") {
            val restriction = HistoryRestriction(List.empty, 2)

            val strategy = ComboRestrictionAssignmentStrategy(List(restriction))

            val assignment = GroupAssignment("2010-01-01")
            assignment.groupA.add("fred")
            assignment.groupA.add("barney")
            assignment.groupA.add("ted")
            assignment.groupA.add("ed")

            assignment.groupB.add("wilma")
            assignment.groupB.add("betty")
            assignment.groupB.add("bob")
            assignment.groupB.add("kate")

            val result = strategy.makeAssignments(assignment, 3)
            assert(result.editorsPerWriterA().keys.size == 4)
            assert(result.editorsPerWriterB().keys.size == 4)
            assert(result.writersPerEditorA().keys.size == 4)
            assert(result.writersPerEditorB().keys.size == 4)
            assert(result.editorsPerWriterA().map(_._2.size).filterNot(_ == 3).isEmpty)
            assert(result.editorsPerWriterB().map(_._2.size).filterNot(_ == 3).isEmpty)
            assert(result.writersPerEditorA().map(_._2.size).filterNot(_ == 3).isEmpty)
            assert(result.writersPerEditorB().map(_._2.size).filterNot(_ == 3).isEmpty)
        }

        scenario("An empty history restriction, no other restrictions, 5 by 4 with 3 per") {
            val restriction = HistoryRestriction(List.empty, 2)

            val strategy = ComboRestrictionAssignmentStrategy(List(restriction))

            val assignment = GroupAssignment("2010-01-01")
            assignment.groupA.add("fred")
            assignment.groupA.add("barney")
            assignment.groupA.add("ted")
            assignment.groupA.add("ed")
            assignment.groupA.add("ned")

            assignment.groupB.add("wilma")
            assignment.groupB.add("betty")
            assignment.groupB.add("bob")
            assignment.groupB.add("kate")

            val result = strategy.makeAssignments(assignment, 3)
            assert(result.editorsPerWriterA().keys.size == 5)
            assert(result.editorsPerWriterB().keys.size == 4)
            assert(result.writersPerEditorB().keys.size == 4)
            assert(result.writersPerEditorA().keys.size == 5)
            assert(result.editorsPerWriterA().map(_._2.size).filterNot(_ == 3).isEmpty)
            assert(result.editorsPerWriterB().map(_._2.size).filterNot(_ == 3).isEmpty)
            assert(result.writersPerEditorB().map(_._2.size).filterNot(s => {s == 3 || s == 4}).isEmpty)
            assert(result.writersPerEditorA().map(_._2.size).filterNot(s => {s == 2 || s == 3}).isEmpty)
        }

        scenario("A single history restriction, no other restrictions") {
            val assignment = GroupAssignment("2010-01-01")
            assignment.groupA.add("fred")
            assignment.groupA.add("barney")
            assignment.groupA.add("ted")
            assignment.groupA.add("ned")

            assignment.groupB.add("wilma")
            assignment.groupB.add("betty")
            assignment.groupB.add("bob")
            assignment.groupB.add("kate")

            assignment.addWriterToEditor("wilma", "fred")
            assignment.addWriterToEditor("betty", "fred")
            assignment.addWriterToEditor("bob", "fred")
            assignment.addWriterToEditor("wilma", "barney")
            assignment.addWriterToEditor("betty", "barney")
            assignment.addWriterToEditor("kate", "barney")
            assignment.addWriterToEditor("wilma", "ted")
            assignment.addWriterToEditor("bob", "ted")
            assignment.addWriterToEditor("kate", "ted")
            assignment.addWriterToEditor("betty", "ned")
            assignment.addWriterToEditor("bob", "ned")
            assignment.addWriterToEditor("kate", "ned")

            assignment.addWriterToEditor("fred", "wilma")
            assignment.addWriterToEditor("barney", "wilma")
            assignment.addWriterToEditor("ted", "wilma")
            assignment.addWriterToEditor("fred", "betty")
            assignment.addWriterToEditor("barney", "betty")
            assignment.addWriterToEditor("ned", "betty")
            assignment.addWriterToEditor("fred", "bob")
            assignment.addWriterToEditor("ted", "bob")
            assignment.addWriterToEditor("ned", "bob")
            assignment.addWriterToEditor("barney", "kate")
            assignment.addWriterToEditor("ted", "kate")
            assignment.addWriterToEditor("ned", "kate")

            val history: List[GroupAssignment] = List(assignment)

            val restriction = HistoryRestriction(history, 1)

            val strategy = ComboRestrictionAssignmentStrategy(List(restriction))

            val newGroup = assignment.copy()
            newGroup.setDate(SimpleDate("2012-01-01").get)

            val result = strategy.makeAssignments(newGroup, 3)

            assert(result.editorsPerWriterA().keys.size == 4)
            assert(result.editorsPerWriterB().keys.size == 4)
            assert(result.writersPerEditorA().keys.size == 4)
            assert(result.writersPerEditorB().keys.size == 4)
            assert(result.editorsPerWriterA().map(_._2.size).filterNot(_ == 3).isEmpty)
            assert(result.editorsPerWriterB().map(_._2.size).filterNot(_ == 3).isEmpty)
            assert(result.writersPerEditorA().map(_._2.size).filterNot(_ == 3).isEmpty)
            assert(result.writersPerEditorB().map(_._2.size).filterNot(_ == 3).isEmpty)

            assert(result.editorsPerWriterA()("fred").diff(List("wilma", "betty", "bob")).size >= 1)
            assert(result.editorsPerWriterA()("barney").diff(List("wilma", "betty", "kate")).size >= 1)
            assert(result.editorsPerWriterA()("ted").diff(List("wilma", "bob", "kate")).size >= 1)
            assert(result.editorsPerWriterA()("ned").diff(List("betty", "bob", "kate")).size >= 1)
            assert(result.editorsPerWriterB()("wilma").diff(List("fred", "barney", "ted")).size >= 1)
            assert(result.editorsPerWriterB()("betty").diff(List("fred", "barney", "ned")).size >= 1)
            assert(result.editorsPerWriterB()("bob").diff(List("fred", "ted", "ned")).size >= 1)
            assert(result.editorsPerWriterB()("kate").diff(List("barney", "ted", "ned")).size >= 1)
        }

        scenario("A double history restriction, no other restrictions") {
            val assignment1 = GroupAssignment("2010-01-01")
            assignment1.groupA.add("fred")
            assignment1.groupA.add("barney")
            assignment1.groupA.add("ted")

            assignment1.groupB.add("wilma")
            assignment1.groupB.add("betty")
            assignment1.groupB.add("bob")

            assignment1.addWriterToEditor("wilma", "fred")
            assignment1.addWriterToEditor("betty", "fred")
            assignment1.addWriterToEditor("wilma", "barney")
            assignment1.addWriterToEditor("bob", "barney")
            assignment1.addWriterToEditor("betty", "ted")
            assignment1.addWriterToEditor("bob", "ted")

            assignment1.addWriterToEditor("fred", "wilma")
            assignment1.addWriterToEditor("barney", "wilma")
            assignment1.addWriterToEditor("fred", "betty")
            assignment1.addWriterToEditor("ted", "betty")
            assignment1.addWriterToEditor("barney", "bob")
            assignment1.addWriterToEditor("ted", "bob")

            val assignment2 = assignment1.copy()
            assignment2.addWriterToEditor("wilma", "fred")
            assignment2.addWriterToEditor("bob", "fred")
            assignment2.addWriterToEditor("betty", "barney")
            assignment2.addWriterToEditor("bob", "barney")
            assignment2.addWriterToEditor("wilma", "ted")
            assignment2.addWriterToEditor("betty", "ted")

            assignment2.addWriterToEditor("fred", "wilma")
            assignment2.addWriterToEditor("ted", "wilma")
            assignment2.addWriterToEditor("barney", "betty")
            assignment2.addWriterToEditor("ted", "betty")
            assignment2.addWriterToEditor("fred", "bob")
            assignment2.addWriterToEditor("barney", "bob")

            val history: List[GroupAssignment] = List(assignment1, assignment2)

            val restriction = HistoryRestriction(history, 1)

            val strategy = ComboRestrictionAssignmentStrategy(List(restriction))

            val newGroup = assignment1.copy()
            newGroup.setDate(SimpleDate("2012-01-01").get)

            val result = strategy.makeAssignments(newGroup, 2)

            assert(result.editorsPerWriterA()("fred").sorted == List("betty", "bob").sorted)
            assert(result.editorsPerWriterA()("barney").sorted == List("betty", "wilma").sorted)
            assert(result.editorsPerWriterA()("ted").sorted == List("wilma", "bob").sorted)
            assert(result.editorsPerWriterB()("wilma").sorted == List("barney", "ted").sorted)
            assert(result.editorsPerWriterB()("betty").sorted == List("fred", "barney").sorted)
            assert(result.editorsPerWriterB()("bob").sorted == List("fred", "ted").sorted)
        }

        scenario("A single history restriction, a BadPair restriction") {
            val badPairRestriction = BadPairRestriction("fred", "wilma")

            val assignment = GroupAssignment("2010-01-01")
            assignment.groupA.add("fred")
            assignment.groupA.add("barney")
            assignment.groupA.add("ted")
            assignment.groupA.add("ned")
            assignment.groupA.add("ed")

            assignment.groupB.add("wilma")
            assignment.groupB.add("betty")
            assignment.groupB.add("bob")
            assignment.groupB.add("kate")
            assignment.groupB.add("nate")

            assignment.addWriterToEditor("wilma", "fred")
            assignment.addWriterToEditor("betty", "fred")
            assignment.addWriterToEditor("bob", "fred")
            assignment.addWriterToEditor("kate", "barney")
            assignment.addWriterToEditor("nate", "barney")
            assignment.addWriterToEditor("wilma", "barney")
            assignment.addWriterToEditor("betty", "ted")
            assignment.addWriterToEditor("bob", "ted")
            assignment.addWriterToEditor("kate", "ted")
            assignment.addWriterToEditor("nate", "ned")
            assignment.addWriterToEditor("wilma", "ned")
            assignment.addWriterToEditor("betty", "ned")
            assignment.addWriterToEditor("bob", "ed")
            assignment.addWriterToEditor("kate", "ed")
            assignment.addWriterToEditor("nate", "ed")

            assignment.addWriterToEditor("fred", "wilma")
            assignment.addWriterToEditor("barney", "wilma")
            assignment.addWriterToEditor("ted", "wilma")
            assignment.addWriterToEditor("ned", "betty")
            assignment.addWriterToEditor("ed", "betty")
            assignment.addWriterToEditor("fred", "betty")
            assignment.addWriterToEditor("barney", "bob")
            assignment.addWriterToEditor("ted", "bob")
            assignment.addWriterToEditor("ned", "bob")
            assignment.addWriterToEditor("ed", "kate")
            assignment.addWriterToEditor("fred", "kate")
            assignment.addWriterToEditor("barney", "kate")
            assignment.addWriterToEditor("ted", "nate")
            assignment.addWriterToEditor("ned", "nate")
            assignment.addWriterToEditor("ed", "nate")

            val history: List[GroupAssignment] = List(assignment)

            val historyRestriction = HistoryRestriction(history, 2)

            val strategy = ComboRestrictionAssignmentStrategy(List(historyRestriction, badPairRestriction))

            val newGroup = assignment.copy()
            newGroup.setDate(SimpleDate("2012-01-01").get)

            val result = strategy.makeAssignments(newGroup, 3)

            assert(result.editorsPerWriterA().keys.size == 5)
            assert(result.editorsPerWriterB().keys.size == 5)
            assert(result.writersPerEditorA().keys.size == 5)
            assert(result.writersPerEditorB().keys.size == 5)
            assert(result.editorsPerWriterA().map(_._2.size).filter(_ < 2).filter(_ > 4).isEmpty)
            assert(result.editorsPerWriterB().map(_._2.size).filter(_ < 2).filter(_ > 4).isEmpty)
            assert(result.writersPerEditorA().map(_._2.size).filter(_ < 2).filter(_ > 4).isEmpty)
            assert(result.writersPerEditorB().map(_._2.size).filter(_ < 2).filter(_ > 4).isEmpty)

            assert(result.editorsPerWriterA()("fred").diff(List("wilma", "betty", "bob")).size >= 2)
            assert(result.editorsPerWriterA()("barney").diff(List("wilma", "nate", "kate")).size >= 2)
            assert(result.editorsPerWriterA()("ted").diff(List("betty", "bob", "kate")).size >= 2)
            assert(result.editorsPerWriterA()("ned").diff(List("nate", "wilma", "betty")).size >= 2)
            assert(result.editorsPerWriterA()("ed").diff(List("bob", "kate", "nate")).size >= 2)
            assert(result.editorsPerWriterB()("wilma").diff(List("fred", "barney", "ted")).size >= 2)
            assert(result.editorsPerWriterB()("betty").diff(List("ned", "ed", "fred")).size >= 2)
            assert(result.editorsPerWriterB()("bob").diff(List("barney", "ted", "ned")).size >= 2)
            assert(result.editorsPerWriterB()("kate").diff(List("ed", "fred", "barney")).size >= 2)
            assert(result.editorsPerWriterB()("nate").diff(List("ted", "ned", "ed")).size >= 2)

            assert(! result.editorsPerWriterA()("fred").contains("wilma"))
            assert(! result.editorsPerWriterB()("wilma").contains("fred"))
        }
    }

    feature("applyEditors adds all the editors in the combo to the writer") {
        scenario("a group assignment without any assignments") {
            val restriction = HistoryRestriction(List.empty, 2)

            val assignment = GroupAssignment("2010-01-01")
            assignment.groupA.add("fred")
            assignment.groupA.add("barney")
            assignment.groupA.add("ted")

            assignment.groupB.add("wilma")
            assignment.groupB.add("betty")
            assignment.groupB.add("ed")

            ComboRestrictionAssignmentStrategy.applyEditors("fred", List("wilma", "ed"), assignment)

            assert(assignment.editorsPerWriterA()("fred").sorted == List("wilma", "ed").sorted)
            assert(assignment.editorsPerWriterB().contains("fred") == false)
            assert(assignment.editorsPerWriterA().contains("wilma") == false)
            assert(assignment.editorsPerWriterA().contains("ed") == false)
            assert(assignment.editorsPerWriterB().contains("wilma") == false)
            assert(assignment.editorsPerWriterB().contains("ed") == false)
        }
    }

    feature("checkCombo checks an editorCombo against restrictions") {
        scenario("an editor combo that violates a history restriction") {

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

            assert(ComboRestrictionAssignmentStrategy.checkCombo("fred", List("wilma", "ed"), List(restriction)) == false)
            assert(ComboRestrictionAssignmentStrategy.checkCombo("fred", List("ted", "ed"), List(restriction)) == true)
        }

        scenario("an editor combo that violates a bad pairs restriction") {
            val assignment = GroupAssignment("2010-01-01")
            assignment.groupA.add("fred")
            assignment.groupA.add("barney")
            assignment.groupA.add("ted")

            assignment.groupB.add("wilma")
            assignment.groupB.add("betty")
            assignment.groupB.add("ed")

            assignment.addWriterToEditor("wilma", "fred")
            assignment.addWriterToEditor("betty", "fred")

            val restriction = BadPairRestriction("fred", "wilma")

            assert(ComboRestrictionAssignmentStrategy.checkCombo("fred", List("wilma", "ed"), List(restriction)) == false)

            assert(ComboRestrictionAssignmentStrategy.checkCombo("fred", List("betty", "ed"), List(restriction)) == true)
        }
    }

    feature("A combo should use all the lesser used editors first") {
        scenario("as many lesser editors as editors per writer") {
            val lessers = List("ed", "ted", "ned")

            assert(ComboRestrictionAssignmentStrategy.checkForLesser(List("ed", "ned", "ted"), lessers) == true)
            assert(ComboRestrictionAssignmentStrategy.checkForLesser(List("ed", "ned", "fred"), lessers) == false)
        }

        scenario("more lesser editors as editors per writer") {
            val lessers = List("ed", "ted", "ned", "fred")

            assert(ComboRestrictionAssignmentStrategy.checkForLesser(List("ed", "ned", "ted"), lessers) == true)
            assert(ComboRestrictionAssignmentStrategy.checkForLesser(List("ed", "ned", "wilma"), lessers) == false)

        }

        scenario("fewer lesser editors as editors per writer") {
            val lessers = List("ed", "ted", "ned")

            assert(ComboRestrictionAssignmentStrategy.checkForLesser(List("ed", "ned", "ted", "wilma"), lessers) == true)
            assert(ComboRestrictionAssignmentStrategy.checkForLesser(List("ed", "ned", "wilma", "betty"), lessers) == false)

        }
    }

    feature("processing one writer should assign a valid combo given the rules") {
        scenario("A history restriction 2 lessers") {
            val writer = "fred"
            val editorCombos = List(List("wilma", "betty"), List("bob", "kate"), List("wilma", "bob"))
            val editorsSoFar = Map("wilma" -> List("fred", "bob"), "betty" -> List("fred", "bob"), "bob" -> List("fred"), "kate" -> List("amy"))

            val assignment = GroupAssignment("2010-01-01")
            assignment.groupA.add("fred")
            assignment.groupA.add("barney")
            assignment.groupA.add("ted")
            assignment.groupA.add("ned")

            assignment.groupB.add("wilma")
            assignment.groupB.add("betty")
            assignment.groupB.add("bob")
            assignment.groupB.add("kate")

            assignment.addWriterToEditor("bob", "fred")
            assignment.addWriterToEditor("kate", "fred")
            assignment.addWriterToEditor("wilma", "barney")
            assignment.addWriterToEditor("betty", "barney")
            assignment.addWriterToEditor("wilma", "ted")
            assignment.addWriterToEditor("bob", "ted")
            assignment.addWriterToEditor("betty", "ned")
            assignment.addWriterToEditor("bob", "ned")

            assignment.addWriterToEditor("fred", "wilma")
            assignment.addWriterToEditor("barney", "wilma")
            assignment.addWriterToEditor("fred", "betty")
            assignment.addWriterToEditor("barney", "betty")
            assignment.addWriterToEditor("fred", "bob")
            assignment.addWriterToEditor("ted", "bob")
            assignment.addWriterToEditor("barney", "kate")
            assignment.addWriterToEditor("ted", "kate")

            val history: List[GroupAssignment] = List(assignment)

            val restriction = HistoryRestriction(history, 1)

            val newGroup = assignment.copy()
            ComboRestrictionAssignmentStrategy.doOneWriter(writer, editorCombos, newGroup, List(restriction), editorsSoFar)

            assert(newGroup.editorsPerWriterA()(writer).sorted == List("wilma", "bob").sorted)
        }
    }
}
