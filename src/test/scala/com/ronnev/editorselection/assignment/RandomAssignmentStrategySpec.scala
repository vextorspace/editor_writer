package com.ronnev.editorselection.assignment

import collection.JavaConverters._
import java.util

import com.ronnev.editorselection.dates.SimpleDate
import org.scalatest.FeatureSpec

class RandomAssignmentStrategySpec extends FeatureSpec {

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

            val newGroup = RandomAssignmentStrategy.makeAssignments(history, emptyGroup, 2)

            val copyGroupA = newGroup.groupA.asScala.toList
            val copyGroupB = newGroup.groupB.asScala.toList

            assert(emptyGroup.groupA.asScala.toList.sorted == copyGroupA.sorted)
            assert(emptyGroup.groupB.asScala.toList.sorted == copyGroupB.sorted)

            val writersA: List[String] = newGroup.assignmentsB.asScala.flatMap(_._2.asScala).toList
            val writersB: List[String] = newGroup.assignmentsA.asScala.flatMap(_._2.asScala).toList

            assert((emptyGroup.groupA.asScala.toList ++ emptyGroup.groupA.asScala.toList).sorted == writersA.sorted)
            assert((emptyGroup.groupB.asScala.toList ++ emptyGroup.groupB.asScala.toList).sorted == writersB.sorted)

            val editorCountsA = newGroup.assignmentsA.asScala.map(ew => (ew._1, ew._2.size))
            val editorCountsB = newGroup.assignmentsA.asScala.map(ew => (ew._1, ew._2.size))
            assert(editorCountsA.values.max <= 2)
            assert(editorCountsB.values.max <= 2)

        }

        def verifyGroup(oneGroup: GroupAssignment, emptyGroup: GroupAssignment) = {
            val writersA: List[String] = oneGroup.assignmentsB.asScala.flatMap(_._2.asScala).toList
            val writersB: List[String] = oneGroup.assignmentsA.asScala.flatMap(_._2.asScala).toList

            assert((emptyGroup.groupA.asScala.toList ++ emptyGroup.groupA.asScala.toList).sorted == writersA.sorted)
            assert((emptyGroup.groupB.asScala.toList ++ emptyGroup.groupB.asScala.toList).sorted == writersB.sorted)

            val editorCountsA = oneGroup.assignmentsA.asScala.map(ew => (ew._1, ew._2.size))
            val editorCountsB = oneGroup.assignmentsA.asScala.map(ew => (ew._1, ew._2.size))
            assert(editorCountsA.values.max <= 2)
            assert(editorCountsB.values.max <= 2)
        }

        scenario("a history with an assignments") {
            val history = new util.ArrayList[GroupAssignment]()
            val emptyGroup = GroupAssignment("2010-07-17")
            emptyGroup.groupA.add("fred")
            emptyGroup.groupA.add("barney")
            emptyGroup.groupA.add("ed")
            emptyGroup.groupB.add("alice")
            emptyGroup.groupB.add("alicia")
            emptyGroup.groupB.add("alison")

            val oneGroup = RandomAssignmentStrategy.makeAssignments(history, emptyGroup, 2)

            verifyGroup(oneGroup, emptyGroup)

            history.add(oneGroup)

            emptyGroup.setDate(SimpleDate("2011-07-17").get)

            val secondGroup = RandomAssignmentStrategy.makeAssignments(history, emptyGroup, 2)

            verifyGroup(secondGroup, emptyGroup)

        }
    }

    feature("a writer gets n different editors in an assignment") {
        scenario("a history with no assignments") {
            val history = new util.ArrayList[GroupAssignment]()

            val emptyGroup = GroupAssignment("2010-07-17")
            emptyGroup.groupA.add("fred")
            emptyGroup.groupA.add("barney")
            emptyGroup.groupA.add("ed")
            emptyGroup.groupA.add("ted")
            emptyGroup.groupB.add("alice")
            emptyGroup.groupB.add("alicia")
            emptyGroup.groupB.add("alison")
            emptyGroup.groupB.add("alvin")

            val oneGroup = RandomAssignmentStrategy.makeAssignments(history, emptyGroup, 3)

            oneGroup.editorsPerWriterA().keys.foreach({ writer =>
                println(s"writer: $writer :")
                oneGroup.editorsPerWriterA()(writer).foreach(editor => print(s" $editor"))
                println("")
            })
            assert(oneGroup.editorsPerWriterA().values.find(editors => editors.distinct.size != editors.size).isEmpty)
            oneGroup.editorsPerWriterB().keys.foreach({ writer =>
                println(s"writer: $writer :")
                oneGroup.editorsPerWriterB()(writer).foreach(editor => print(s" $editor"))
                println("")
            })
            assert(oneGroup.editorsPerWriterB().values.find(editors => editors.distinct.size != editors.size).isEmpty)
        }
    }

    feature("an editor gets the ceil or floor of the number of editors per writer") {
        scenario("4 editors 4 writers, 2 editors per writer every editor gets 2 writers") {
            val history = new util.ArrayList[GroupAssignment]()

            val emptyGroup = GroupAssignment("2010-07-17")
            emptyGroup.groupA.add("fred")
            emptyGroup.groupA.add("barney")
            emptyGroup.groupA.add("ed")
            emptyGroup.groupA.add("ted")
            emptyGroup.groupB.add("alice")
            emptyGroup.groupB.add("alicia")
            emptyGroup.groupB.add("alison")
            emptyGroup.groupB.add("alvin")

            val oneGroup = RandomAssignmentStrategy.makeAssignments(history, emptyGroup, 2)
            assert(oneGroup.writersPerEditorB().values.find(editors => editors.size != 2).isEmpty)
            assert(oneGroup.writersPerEditorA().values.find(editors => editors.size != 2).isEmpty)

        }

        scenario("4 editors 4 writers, 3 editors per writer every editor gets 3 writers") {
            val history = new util.ArrayList[GroupAssignment]()

            val emptyGroup = GroupAssignment("2010-07-17")
            emptyGroup.groupA.add("fred")
            emptyGroup.groupA.add("barney")
            emptyGroup.groupA.add("ed")
            emptyGroup.groupA.add("ted")
            emptyGroup.groupB.add("alice")
            emptyGroup.groupB.add("alicia")
            emptyGroup.groupB.add("alison")
            emptyGroup.groupB.add("alvin")

            val oneGroup = RandomAssignmentStrategy.makeAssignments(history, emptyGroup, 3)
            assert(oneGroup.writersPerEditorB().values.find(editors => editors.size != 3).isEmpty)
            assert(oneGroup.writersPerEditorA().values.find(editors => editors.size != 3).isEmpty)
        }

        scenario("4 editors 5 writers, 3 editors per writer every editor gets 3 writers or 4") {
            val history = new util.ArrayList[GroupAssignment]()

            val emptyGroup = GroupAssignment("2010-07-17")
            emptyGroup.groupA.add("fred")
            emptyGroup.groupA.add("barney")
            emptyGroup.groupA.add("ed")
            emptyGroup.groupA.add("ted")
            emptyGroup.groupA.add("ned")
            emptyGroup.groupB.add("alice")
            emptyGroup.groupB.add("alicia")
            emptyGroup.groupB.add("alison")
            emptyGroup.groupB.add("alvin")

            val oneGroup = RandomAssignmentStrategy.makeAssignments(history, emptyGroup, 3)

            println("---- writers per editor")
            println("-- group a")
            oneGroup.writersPerEditorA().foreach(writer => println(s" $writer"))

            println("-- group b")
            oneGroup.writersPerEditorB().foreach(writer => println(s" $writer"))

            println("---- editors per writer")
            println("-- group a")
            oneGroup.editorsPerWriterA().foreach(writer => println(s" $writer"))

            println("-- group b")
            oneGroup.editorsPerWriterB().foreach(writer => println(s" $writer"))

            assert(oneGroup.writersPerEditorA().values.find(editors => !(editors.size == 3 || editors.size == 4)).isEmpty)
        }
    }

    feature("computes whether there are any overloaded editors that should no longer be used") {
        scenario("no writers per editor") {
            val emptyGroup = GroupAssignment("2010-07-17")
            emptyGroup.groupA.add("fred")
            emptyGroup.groupA.add("barney")
            emptyGroup.groupA.add("ed")
            emptyGroup.groupA.add("ted")
            emptyGroup.groupA.add("ned")
            emptyGroup.groupB.add("alice")
            emptyGroup.groupB.add("alicia")
            emptyGroup.groupB.add("alison")
            emptyGroup.groupB.add("alvin")

            assert(RandomAssignmentStrategy.overloadedEditors(emptyGroup.writersPerEditorA(), 2) == List.empty[String])
            assert(RandomAssignmentStrategy.overloadedEditors(emptyGroup.writersPerEditorB(), 2) == List.empty[String])
        }

        scenario("an editor that has as many writers as the max") {
            val group = GroupAssignment("2010-07-17")
            group.groupA.add("fred")
            group.groupA.add("barney")
            group.groupA.add("ed")
            group.groupA.add("ted")
            group.groupA.add("ned")
            group.groupB.add("alice")
            group.groupB.add("alicia")
            group.groupB.add("alison")
            group.groupB.add("alvin")

            group.addWriterToEditor("fred", "alice")
            group.addWriterToEditor("fred", "alvin")
            group.addWriterToEditor("barney", "alicia")

            assert(RandomAssignmentStrategy.overloadedEditors(group.writersPerEditorB(), 2) == List.empty[String])
            assert(RandomAssignmentStrategy.overloadedEditors(group.writersPerEditorA(), 2) == List("fred"))

        }

        scenario("an editor that has more writers than the max") {
            val group = GroupAssignment("2010-07-17")
            group.groupA.add("fred")
            group.groupA.add("barney")
            group.groupA.add("ed")
            group.groupA.add("ted")
            group.groupA.add("ned")
            group.groupB.add("alice")
            group.groupB.add("alicia")
            group.groupB.add("alison")
            group.groupB.add("alvin")

            group.addWriterToEditor("fred", "alice")
            group.addWriterToEditor("fred", "alvin")
            group.addWriterToEditor("barney", "alicia")

            group.addWriterToEditor("alice", "fred")
            group.addWriterToEditor("alice", "barney")
            group.addWriterToEditor("alice", "ted")
            group.addWriterToEditor("alice", "ned")
            group.addWriterToEditor("alicia", "fred")
            group.addWriterToEditor("alicia", "barney")
            assert(RandomAssignmentStrategy.overloadedEditors(group.writersPerEditorB(), 3) == List("alice"))
            assert(RandomAssignmentStrategy.overloadedEditors(group.writersPerEditorA(), 3) == List.empty[String])
        }

        scenario("three editors with as many writers as the max") {
            val group = GroupAssignment("2010-07-17")
            group.groupA.add("fred")
            group.groupA.add("barney")
            group.groupA.add("ed")
            group.groupA.add("ted")
            group.groupA.add("ned")
            group.groupB.add("alice")
            group.groupB.add("alicia")
            group.groupB.add("alison")
            group.groupB.add("alvin")

            group.addWriterToEditor("fred", "alice")
            group.addWriterToEditor("fred", "alvin")
            group.addWriterToEditor("fred", "alison")
            group.addWriterToEditor("barney", "alicia")

            group.addWriterToEditor("alice", "fred")
            group.addWriterToEditor("alice", "barney")
            group.addWriterToEditor("alice", "ted")
            group.addWriterToEditor("alice", "ned")
            group.addWriterToEditor("alicia", "fred")
            group.addWriterToEditor("alicia", "barney")
            group.addWriterToEditor("alicia", "ted")
            assert(RandomAssignmentStrategy.overloadedEditors(group.writersPerEditorB(), 3).sorted == List("alice", "alicia").sorted)
            assert(RandomAssignmentStrategy.overloadedEditors(group.writersPerEditorA(), 3) == List("fred"))

        }
    }

    feature("editors per writer are assigned out of list given") {
        scenario("5 editors 2 editors per writer") {
            val editors = List("fred", "ted", "ed", "ned", "bob")

            val editorsPerWriter = RandomAssignmentStrategy.createRandomEditorList(editors, 2)

            assert(editorsPerWriter.size == 2)
            assert(editorsPerWriter.distinct.size == 2)
            assert((editors diff editorsPerWriter).size == 3)
        }
    }
}
