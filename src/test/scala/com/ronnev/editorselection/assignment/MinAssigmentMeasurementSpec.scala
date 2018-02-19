package com.ronnev.editorselection.assignment

import java.util

import org.scalatest.FeatureSpec

class MinAssigmentMeasurementSpec extends FeatureSpec {
    feature("can find the different pairs between two maps of writers to sets of editors") {
        scenario("A list that is the same has no difference") {
            val firstGroup = GroupAssignment("2010-09-08")

            firstGroup.groupA.add("fred")
            firstGroup.groupA.add("barney")
            firstGroup.groupB.add("ted")
            firstGroup.groupB.add("ed")
            firstGroup.groupB.add("melissa")
            firstGroup.groupB.add("mel")
            firstGroup.groupB.add("kate")

            firstGroup.addWriterToEditor("fred", "ted")
            firstGroup.addWriterToEditor("fred", "ed")
            firstGroup.addWriterToEditor("barney", "melissa")
            firstGroup.addWriterToEditor("barney", "mel")

            val secondGroup = firstGroup.copy()
            secondGroup.addWriterToEditor("fred", "ted")
            secondGroup.addWriterToEditor("fred", "ed")
            secondGroup.addWriterToEditor("barney", "melissa")
            secondGroup.addWriterToEditor("barney", "mel")

            assert(MinAssigmentMeasurement.findAddedPairs(firstGroup.editorsPerWriterA(), secondGroup.editorsPerWriterA()).isEmpty)
        }

        scenario("Lists that have one thing removed have no added") {
            val firstGroup = GroupAssignment("2010-09-08")

            firstGroup.groupA.add("fred")
            firstGroup.groupA.add("barney")
            firstGroup.groupB.add("ted")
            firstGroup.groupB.add("ed")
            firstGroup.groupB.add("melissa")
            firstGroup.groupB.add("mel")
            firstGroup.groupB.add("kate")

            firstGroup.addWriterToEditor("fred", "ted")
            firstGroup.addWriterToEditor("fred", "ed")
            firstGroup.addWriterToEditor("barney", "melissa")
            firstGroup.addWriterToEditor("barney", "mel")

            val secondGroup = firstGroup.copy()
            secondGroup.addWriterToEditor("fred", "ted")
            secondGroup.addWriterToEditor("fred", "ed")
            secondGroup.addWriterToEditor("barney", "melissa")

            assert(MinAssigmentMeasurement.findAddedPairs(firstGroup.editorsPerWriterB(), secondGroup.editorsPerWriterB()) == Map("ted" -> List.empty[String], "ed" -> List.empty[String], "melissa" -> List.empty[String]))

            assert(MinAssigmentMeasurement.findAddedPairs(secondGroup.editorsPerWriterB(), firstGroup.editorsPerWriterB()) == Map("ted" -> List.empty[String], "ed" -> List.empty[String], "melissa" -> List.empty[String], "mel" -> List("barney")))
        }

        scenario("Lists that have one thing added have 1 added") {
            val firstGroup = GroupAssignment("2010-09-08")

            firstGroup.groupA.add("fred")
            firstGroup.groupA.add("barney")
            firstGroup.groupB.add("ted")
            firstGroup.groupB.add("ed")
            firstGroup.groupB.add("melissa")
            firstGroup.groupB.add("mel")
            firstGroup.groupB.add("kate")

            firstGroup.addWriterToEditor("fred", "ted")
            firstGroup.addWriterToEditor("barney", "melissa")
            firstGroup.addWriterToEditor("barney", "mel")

            val secondGroup = firstGroup.copy()
            secondGroup.addWriterToEditor("fred", "ted")
            secondGroup.addWriterToEditor("fred", "ed")
            secondGroup.addWriterToEditor("barney", "melissa")
            secondGroup.addWriterToEditor("barney", "mel")

            assert(MinAssigmentMeasurement.findAddedPairs(firstGroup.editorsPerWriterB(), secondGroup.editorsPerWriterB()) == Map("ted"->List.empty[String], "ed" -> List("fred"), "melissa" -> List.empty[String], "mel" -> List.empty[String]))
        }
    }

    feature("groupToGroupMinimm measures the smallest number of different assignments for writers between groups") {
        scenario("Two groups that are the same") {
            val firstGroup = GroupAssignment("2010-09-08")

            firstGroup.groupA.add("fred")
            firstGroup.groupA.add("barney")
            firstGroup.groupB.add("ted")
            firstGroup.groupB.add("ed")
            firstGroup.groupB.add("melissa")
            firstGroup.groupB.add("mel")
            firstGroup.groupB.add("kate")

            firstGroup.addWriterToEditor("fred", "ted")
            firstGroup.addWriterToEditor("fred", "ed")
            firstGroup.addWriterToEditor("barney", "melissa")
            firstGroup.addWriterToEditor("barney", "mel")

            val secondGroup = firstGroup.copy()
            secondGroup.addWriterToEditor("fred", "ted")
            secondGroup.addWriterToEditor("fred", "ed")
            secondGroup.addWriterToEditor("barney", "melissa")
            secondGroup.addWriterToEditor("barney", "mel")

            assert(MinAssigmentMeasurement.groupToGroupMinimum(firstGroup, secondGroup) == 0)
        }

        scenario("Two groups that have 1 added for each writer") {
            val firstGroup = GroupAssignment("2010-09-08")

            firstGroup.groupA.add("fred")
            firstGroup.groupA.add("barney")
            firstGroup.groupA.add("betty")
            firstGroup.groupA.add("wilma")
            firstGroup.groupB.add("ted")
            firstGroup.groupB.add("ed")
            firstGroup.groupB.add("melissa")
            firstGroup.groupB.add("mel")

            firstGroup.addWriterToEditor("fred", "ted")
            firstGroup.addWriterToEditor("barney", "ed")
            firstGroup.addWriterToEditor("betty", "melissa")
            firstGroup.addWriterToEditor("wilma", "mel")
            firstGroup.addWriterToEditor("ted", "fred")
            firstGroup.addWriterToEditor("ed", "barney")
            firstGroup.addWriterToEditor("melissa", "betty")
            firstGroup.addWriterToEditor("mel", "wilma")


            val secondGroup = firstGroup.copy()
            secondGroup.addWriterToEditor("fred", "ted")
            secondGroup.addWriterToEditor("fred", "ed")
            secondGroup.addWriterToEditor("barney", "ted")
            secondGroup.addWriterToEditor("barney", "ed")
            secondGroup.addWriterToEditor("betty", "melissa")
            secondGroup.addWriterToEditor("betty", "mel")
            secondGroup.addWriterToEditor("wilma", "melissa")
            secondGroup.addWriterToEditor("wilma", "mel")
            secondGroup.addWriterToEditor("ted", "fred")
            secondGroup.addWriterToEditor("ed", "barney")
            secondGroup.addWriterToEditor("melissa", "betty")
            secondGroup.addWriterToEditor("mel", "wilma")
            secondGroup.addWriterToEditor("ted", "barney")
            secondGroup.addWriterToEditor("ed", "fred")
            secondGroup.addWriterToEditor("melissa", "wilma")
            secondGroup.addWriterToEditor("mel", "betty")

            assert(MinAssigmentMeasurement.groupToGroupMinimum(firstGroup, secondGroup) == 1)
        }

        scenario("Two empty groups") {
            val firstGroup = GroupAssignment("2010-09-08")

            firstGroup.groupA.add("fred")
            firstGroup.groupA.add("barney")
            firstGroup.groupB.add("ted")
            firstGroup.groupB.add("ed")
            firstGroup.groupB.add("melissa")
            firstGroup.groupB.add("mel")
            firstGroup.groupB.add("kate")

            val secondGroup = firstGroup.copy()

            assert(MinAssigmentMeasurement.groupToGroupMinimum(firstGroup, secondGroup) == 0)
        }

        scenario("One empty group for first") {
            val firstGroup = GroupAssignment("2010-09-08")

            firstGroup.groupA.add("fred")
            firstGroup.groupA.add("barney")
            firstGroup.groupB.add("ted")
            firstGroup.groupB.add("ed")
            firstGroup.groupB.add("melissa")
            firstGroup.groupB.add("mel")
            firstGroup.groupB.add("kate")

            val secondGroup = firstGroup.copy()

            secondGroup.addWriterToEditor("fred", "ted")
            secondGroup.addWriterToEditor("fred", "ed")
            secondGroup.addWriterToEditor("barney", "melissa")
            secondGroup.addWriterToEditor("barney", "mel")

            secondGroup.addWriterToEditor("melissa", "fred")
            secondGroup.addWriterToEditor("mel", "barney")


            assert(MinAssigmentMeasurement.groupToGroupMinimum(firstGroup, secondGroup) == 1)
        }

        scenario("One empty group for second") {
            val firstGroup = GroupAssignment("2010-09-08")

            firstGroup.groupA.add("fred")
            firstGroup.groupA.add("barney")
            firstGroup.groupB.add("ted")
            firstGroup.groupB.add("ed")
            firstGroup.groupB.add("melissa")
            firstGroup.groupB.add("mel")
            firstGroup.groupB.add("kate")

            firstGroup.addWriterToEditor("fred", "ted")
            firstGroup.addWriterToEditor("fred", "ed")
            firstGroup.addWriterToEditor("barney", "melissa")
            firstGroup.addWriterToEditor("barney", "mel")
            firstGroup.addWriterToEditor("melissa", "fred")
            firstGroup.addWriterToEditor("mel", "barney")

            val secondGroup = firstGroup.copy()

            assert(MinAssigmentMeasurement.groupToGroupMinimum(firstGroup, secondGroup) == 0)
        }

        scenario("Two lists that have 1 added for each writer") {
            val firstGroup = GroupAssignment("2010-09-08")

            firstGroup.groupA.add("fred")
            firstGroup.groupA.add("barney")
            firstGroup.groupA.add("betty")
            firstGroup.groupA.add("wilma")
            firstGroup.groupB.add("ted")
            firstGroup.groupB.add("ed")
            firstGroup.groupB.add("melissa")
            firstGroup.groupB.add("mel")

            firstGroup.addWriterToEditor("fred", "ted")
            firstGroup.addWriterToEditor("barney", "ed")
            firstGroup.addWriterToEditor("betty", "melissa")
            firstGroup.addWriterToEditor("wilma", "mel")
            firstGroup.addWriterToEditor("ted", "fred")
            firstGroup.addWriterToEditor("ed", "barney")
            firstGroup.addWriterToEditor("melissa", "betty")
            firstGroup.addWriterToEditor("mel", "wilma")


            val secondGroup = firstGroup.copy()
            secondGroup.addWriterToEditor("fred", "ted")
            secondGroup.addWriterToEditor("fred", "ed")
            secondGroup.addWriterToEditor("barney", "ted")
            secondGroup.addWriterToEditor("barney", "ed")
            secondGroup.addWriterToEditor("betty", "melissa")
            secondGroup.addWriterToEditor("betty", "mel")
            secondGroup.addWriterToEditor("wilma", "melissa")
            secondGroup.addWriterToEditor("wilma", "mel")
            firstGroup.addWriterToEditor("ted", "fred")
            firstGroup.addWriterToEditor("ed", "barney")
            firstGroup.addWriterToEditor("melissa", "betty")
            firstGroup.addWriterToEditor("mel", "wilma")
            firstGroup.addWriterToEditor("ted", "barney")
            firstGroup.addWriterToEditor("ed", "fred")
            firstGroup.addWriterToEditor("melissa", "wilma")
            firstGroup.addWriterToEditor("mel", "betty")

            assert(MinAssigmentMeasurement.findAddedPairs(firstGroup.editorsPerWriterB(), secondGroup.editorsPerWriterB()) == Map("ed" -> List("fred"), "ted" -> List("barney"), "mel" -> List("betty"), "melissa" -> List("wilma")))
        }

        scenario("Another Two lists that have 1 added for each writer") {
            val firstGroup = GroupAssignment("2010-09-08")

            firstGroup.groupA.add("fred")
            firstGroup.groupA.add("barney")
            firstGroup.groupA.add("betty")
            firstGroup.groupA.add("wilma")
            firstGroup.groupB.add("ted")
            firstGroup.groupB.add("ed")
            firstGroup.groupB.add("melissa")
            firstGroup.groupB.add("mel")

            firstGroup.addWriterToEditor("fred", "ted")
            firstGroup.addWriterToEditor("barney", "ed")
            firstGroup.addWriterToEditor("betty", "melissa")
            firstGroup.addWriterToEditor("wilma", "mel")
            firstGroup.addWriterToEditor("ted", "fred")
            firstGroup.addWriterToEditor("ed", "barney")
            firstGroup.addWriterToEditor("melissa", "betty")
            firstGroup.addWriterToEditor("mel", "wilma")


            val secondGroup = firstGroup.copy()
            secondGroup.addWriterToEditor("fred", "ted")
            secondGroup.addWriterToEditor("fred", "ed")
            secondGroup.addWriterToEditor("barney", "ted")
            secondGroup.addWriterToEditor("barney", "ed")
            secondGroup.addWriterToEditor("betty", "melissa")
            secondGroup.addWriterToEditor("betty", "mel")
            secondGroup.addWriterToEditor("wilma", "melissa")
            secondGroup.addWriterToEditor("wilma", "mel")
            secondGroup.addWriterToEditor("ted", "fred")
            secondGroup.addWriterToEditor("ed", "barney")
            secondGroup.addWriterToEditor("melissa", "betty")
            secondGroup.addWriterToEditor("mel", "wilma")
            secondGroup.addWriterToEditor("ted", "barney")
            secondGroup.addWriterToEditor("ed", "fred")
            secondGroup.addWriterToEditor("melissa", "wilma")
            secondGroup.addWriterToEditor("mel", "betty")

            assert(MinAssigmentMeasurement.findAddedPairs(firstGroup.editorsPerWriterA(), secondGroup.editorsPerWriterA()) == Map("fred" -> List("ed"), "barney" -> List("ted"), "betty" -> List("mel"), "wilma" -> List("melissa")))
        }

    }

    feature("MinAssigmnent measures the least added editors for any writer to any histories") {
        scenario("One assigment history and a new assigment with one that is the same") {
            val firstGroup = GroupAssignment("2010-09-08")

            firstGroup.groupA.add("fred")
            firstGroup.groupA.add("barney")
            firstGroup.groupB.add("ted")
            firstGroup.groupB.add("ed")
            firstGroup.groupB.add("melissa")
            firstGroup.groupB.add("mel")
            firstGroup.groupB.add("kate")

            firstGroup.addWriterToEditor("fred", "ted")
            firstGroup.addWriterToEditor("fred", "ed")
            firstGroup.addWriterToEditor("barney", "melissa")
            firstGroup.addWriterToEditor("barney", "mel")

            val secondGroup = firstGroup.copy()
            secondGroup.addWriterToEditor("fred", "ted")
            secondGroup.addWriterToEditor("fred", "ed")
            secondGroup.addWriterToEditor("barney", "melissa")
            secondGroup.addWriterToEditor("barney", "mel")

            val history = new util.ArrayList[GroupAssignment]()
            history.add(firstGroup)

            val result = MinAssigmentMeasurement.measurement(history, secondGroup)

            assert(result == 0)
        }

        scenario("One assigment history and a new assigment with one that is two different and one that is two") {
            val firstGroup = GroupAssignment("2010-09-08")

            firstGroup.groupA.add("fred")
            firstGroup.groupA.add("barney")
            firstGroup.groupA.add("betty")
            firstGroup.groupA.add("wilma")
            firstGroup.groupB.add("ted")
            firstGroup.groupB.add("ed")
            firstGroup.groupB.add("melissa")
            firstGroup.groupB.add("mel")

            firstGroup.addWriterToEditor("fred", "ted")
            firstGroup.addWriterToEditor("barney", "ed")
            firstGroup.addWriterToEditor("betty", "melissa")
            firstGroup.addWriterToEditor("wilma", "mel")
            firstGroup.addWriterToEditor("ted", "fred")
            firstGroup.addWriterToEditor("ed", "barney")
            firstGroup.addWriterToEditor("melissa", "betty")
            firstGroup.addWriterToEditor("mel", "wilma")

            val secondGroup = firstGroup.copy()

            secondGroup.addWriterToEditor("fred", "ted")
            secondGroup.addWriterToEditor("fred", "ed")
            secondGroup.addWriterToEditor("barney", "ted")
            secondGroup.addWriterToEditor("barney", "ed")
            secondGroup.addWriterToEditor("betty", "melissa")
            secondGroup.addWriterToEditor("betty", "mel")
            secondGroup.addWriterToEditor("wilma", "melissa")
            secondGroup.addWriterToEditor("wilma", "mel")
            secondGroup.addWriterToEditor("ted", "fred")
            secondGroup.addWriterToEditor("ed", "barney")
            secondGroup.addWriterToEditor("melissa", "betty")
            secondGroup.addWriterToEditor("mel", "wilma")
            secondGroup.addWriterToEditor("ted", "barney")
            secondGroup.addWriterToEditor("ed", "fred")
            secondGroup.addWriterToEditor("melissa", "wilma")
            secondGroup.addWriterToEditor("mel", "betty")
            secondGroup.addWriterToEditor("mel", "barney")

            val history = new util.ArrayList[GroupAssignment]()
            history.add(firstGroup)

            val result = MinAssigmentMeasurement.measurement(history, secondGroup)

            assert(result == 1.0)

            val secondHistory = new util.ArrayList[GroupAssignment]()
            history.add(secondGroup)
            val oppositeResult = MinAssigmentMeasurement.measurement(history, firstGroup)
            assert(oppositeResult == 0.0)
        }

    }
}
