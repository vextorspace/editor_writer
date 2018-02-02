package com.ronnev.editorselection.assignment

import collection.JavaConverters._
import java.util

import com.ronnev.editorselection.SimpleDate
import org.scalatest.FeatureSpec

class RandomAssigmentStrategySpec extends FeatureSpec {
    feature("A random assigment strategy randomly assigns") {
        scenario("An empty history") {
            val history = new util.ArrayList[GroupAssignment]()
            val emptyGroup = GroupAssignment("2010-07-17")
            emptyGroup.groupA.add("fred")
            emptyGroup.groupA.add("barney")
            emptyGroup.groupA.add("ed")
            emptyGroup.groupB.add("alice")
            emptyGroup.groupB.add("alicia")
            emptyGroup.groupB.add("alison")

            val newGroup = RandomAssigmentStrategy.makeAssigments(history, emptyGroup, 2)

            val writersA: List[String] = newGroup.assignmentsA.asScala.flatMap(_._2.asScala).toList
            val writersB: List[String] = newGroup.assignmentsB.asScala.flatMap(_._2.asScala).toList

            assert(emptyGroup.groupA.asScala.toList.sorted == writersA.sorted)
            assert(emptyGroup.groupB.asScala.toList.sorted == writersB.sorted)

            val editorCountsA = newGroup.assignmentsA.asScala.map(ew => (ew._1, ew._2.size.toInt))
            val editorCountsB = newGroup.assignmentsA.asScala.map(ew => (ew._1, ew._2.size.toInt))
            assert(editorCountsA.values.max <= 2)
            assert(editorCountsB.values.max <= 2)

        }

        def verifyGroup(oneGroup: GroupAssignment, emptyGroup: GroupAssignment) = {
            val writersA: List[String] = oneGroup.assignmentsA.asScala.flatMap(_._2.asScala).toList
            val writersB: List[String] = oneGroup.assignmentsB.asScala.flatMap(_._2.asScala).toList

            assert(emptyGroup.groupA.asScala.toList.sorted == writersA.sorted)
            assert(emptyGroup.groupB.asScala.toList.sorted == writersB.sorted)

            val editorCountsA = oneGroup.assignmentsA.asScala.map(ew => (ew._1, ew._2.size.toInt))
            val editorCountsB = oneGroup.assignmentsA.asScala.map(ew => (ew._1, ew._2.size.toInt))
            assert(editorCountsA.values.max <= 2)
            assert(editorCountsB.values.max <= 2)
        }

        scenario("a history with an assigmnents") {
            val history = new util.ArrayList[GroupAssignment]()
            var emptyGroup = GroupAssignment("2010-07-17")
            emptyGroup.groupA.add("fred")
            emptyGroup.groupA.add("barney")
            emptyGroup.groupA.add("ed")
            emptyGroup.groupB.add("alice")
            emptyGroup.groupB.add("alicia")
            emptyGroup.groupB.add("alison")

            val oneGroup = RandomAssigmentStrategy.makeAssigments(history, emptyGroup, 2)

            verifyGroup(oneGroup, emptyGroup)

            history.add(oneGroup)

            emptyGroup.setDate(SimpleDate("2011-07-17").get)

            val secondGroup = RandomAssigmentStrategy.makeAssigments(history, emptyGroup, 2)

            verifyGroup(secondGroup, emptyGroup)

        }
    }

    feature("")
}
