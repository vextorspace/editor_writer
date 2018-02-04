package com.ronnev.editorselection.assignment

import collection.JavaConverters._
import java.util

import com.ronnev.editorselection.SimpleDate
import org.scalatest.FeatureSpec

class RandomAssigmentStrategySpec extends FeatureSpec {

    feature("removes one item from a list if it exists") {
        scenario("A list with the same thing repeated") {
            val list = List("bob", "ted", "ted", "ed")

            val newList = RandomAssigmentStrategy.removeOneFromList(list, "ted")

            assert(newList == List("bob", "ted", "ed"))
        }

        scenario("A list with only one thing, removing wrong item") {
            val list = List("alicia")

            val newList = RandomAssigmentStrategy.removeOneFromList(list, "alici")

            assert(newList == list)
        }

        scenario("A list with only one thing, removing item") {
            val list = List("alicia")

            val secondList = RandomAssigmentStrategy.removeOneFromList(list, "alicia")

            assert(secondList.isEmpty)
        }

        scenario("A list with only one thing, removing empty item") {
            val list = List("alicia")

            val secondList = RandomAssigmentStrategy.removeOneFromList(list, "")

            assert(secondList == list)
        }
    }

    feature("random editor selection can select anything from a list") {
        scenario("A list with ten things") {
            val list = List("fred", "fred", "fred", "ted", "ed", "barney", "wilma", "wilma", "wilma", "wilma")
            var freds: Double = 0
            var teds: Double = 0
            var eds: Double = 0
            var barneys: Double = 0
            var wilmas: Double = 0

            val trials = 10000

            1 to 10000 foreach {_ =>
                RandomAssigmentStrategy.randomEditor(list) match {
                    case "fred" => freds+=1
                    case "ted" => teds+=1
                    case "ed" => eds+=1
                    case "barney" => barneys+=1
                    case "wilma" => wilmas+=1
                }
            }

            println(s"freds: $freds teds: $teds eds: $eds barneys: $barneys wilmas: $wilmas")

            assert(Math.abs(freds - .3*trials)/trials < .01)

            assert(Math.abs(teds - .1*trials)/trials < .01)

            assert(Math.abs(eds - .1*trials)/trials < .01)

            assert(Math.abs(barneys - .1*trials)/trials < .01)

            assert(Math.abs(wilmas - .4*trials)/trials < .01)
        }
    }

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

            val copyGroupA = newGroup.groupA.asScala.toList
            val copyGroupB = newGroup.groupB.asScala.toList

            assert(emptyGroup.groupA.asScala.toList.sorted == copyGroupA.sorted)
            assert(emptyGroup.groupB.asScala.toList.sorted == copyGroupB.sorted)

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
            val emptyGroup = GroupAssignment("2010-07-17")
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
}
