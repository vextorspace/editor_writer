package com.ronnev.editorselection.assignment

import java.util

import org.scalatest.FeatureSpec

class MinAssigmentMeasurementSpec extends FeatureSpec {
    feature("MinAssigmnent measures the least difference for any writer to any histories") {
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
            secondGroup.addWriterToEditor("barney", "kate")

            val history = new util.ArrayList[GroupAssignment]()
            history.add(firstGroup)

            val result = MinAssigmentMeasurement.measurement(history, secondGroup)

            assert(result == 0)
        }

        scenario("One assigment history and a new assigment with one that is three different and one that is two") {
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
            secondGroup.addWriterToEditor("fred", "melissa")
            secondGroup.addWriterToEditor("fred", "mel")
            secondGroup.addWriterToEditor("fred", "kate")
            secondGroup.addWriterToEditor("barney", "ted")
            secondGroup.addWriterToEditor("barney", "ed")

            val history = new util.ArrayList[GroupAssignment]()
            history.add(firstGroup)

            val result = MinAssigmentMeasurement.measurement(history, secondGroup)

            assert(result == 2)
        }

    }
}
