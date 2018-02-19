package com.ronnev.editorselection.assignment

import java.util

import com.ronnev.editorselection.dates.SimpleDate
import org.scalatest.FeatureSpec
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.{DumperOptions, Yaml}

class GroupAssignmentSpec extends FeatureSpec {

    feature("A GroupAssignment loads from text") {
        scenario("An empty text makes an empty group assignment") {

            val text =
                """
                  |groupA: []
                  |groupB: []
                  |assignmentsA: {}
                  |assignmentsB: {}
                  |date:
                  |  year: 2010
                  |  month: 01
                  |  day: 01
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[GroupAssignment]))
            val result = yaml.load(text).asInstanceOf[GroupAssignment]

            assert(result.assignmentsA.isEmpty)
            assert(result.assignmentsB.isEmpty)
            assert(result.groupA.isEmpty)
            assert(result.groupB.isEmpty)
            assert(result.date == SimpleDate("2010-01-01").get)
        }

        scenario("A normal group assignment loads from text") {

            val text =
                """
                  |groupA: ["fred", "barney", "wilma"]
                  |groupB: ["betty", "kate", "bob", "ted", "ed"]
                  |assignmentsA: {"fred": ["betty", "kate"], "barney": ["bob", "ted"]}
                  |assignmentsB: {"betty": ["fred", "barney", "wilma"]}
                  |date:
                  |  year: 2017
                  |  month: 07
                  |  day: 17
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[GroupAssignment]))
            val result = yaml.load(text).asInstanceOf[GroupAssignment]

            assert(result.assignmentsA.size() == 2)
            assert(result.assignmentsA.get("fred").toArray.toList == List("betty", "kate"))
            assert(result.assignmentsA.get("barney").toArray.toList == List("bob", "ted"))
            assert(result.assignmentsB.get("betty").toArray.toList == List("fred", "barney", "wilma"))
            assert(result.groupA.toArray.toList == List("fred", "barney", "wilma"))
            assert(result.groupB.toArray.toList == List("betty", "kate", "bob", "ted", "ed"))
            assert(result.date == SimpleDate("2017-07-17").get)
        }
    }

    feature("A GroupAssignment stores and loads back the same") {
        scenario("An empty group assignment") {

            val group: GroupAssignment = GroupAssignment()

            val yaml = new Yaml()
            val groupString = yaml.dumpAs(group, Tag.MAP, DumperOptions.FlowStyle.AUTO)
            val yaml2 = new Yaml(new Constructor(classOf[GroupAssignment]))
            val result = yaml2.load(groupString).asInstanceOf[GroupAssignment]

            assert(result.groupA == group.groupA)
            assert(result.groupB == group.groupB)
            assert(result.assignmentsA == group.assignmentsA)
            assert(result.assignmentsB == group.assignmentsB)
            assert(result.date == group.date)

        }

        scenario("A non-empty group assignment") {

            val group: GroupAssignment = GroupAssignment("2012-20-12")
            group.groupA.add("fred")

            group.groupB.add("barney")
            group.groupB.add("wilma")

            val fredsAssignments = new util.ArrayList[String]()
            fredsAssignments.add("barney")
            fredsAssignments.add("wilma")
            group.assignmentsA.put("fred", fredsAssignments)

            val bAssignments = new util.ArrayList[String]()
            bAssignments.add("fred")

            val wAssignments = new util.ArrayList[String]()
            wAssignments.add("fred")

            group.assignmentsB.put("barney", bAssignments)
            group.assignmentsA.put("wilma", wAssignments)

            val yaml = new Yaml()
            val groupString = yaml.dumpAs(group, Tag.MAP, DumperOptions.FlowStyle.AUTO)
            val yaml2 = new Yaml(new Constructor(classOf[GroupAssignment]))
            val result = yaml2.load(groupString).asInstanceOf[GroupAssignment]

            assert(result.groupA == group.groupA)
            assert(result.groupB == group.groupB)
            assert(result.assignmentsA == group.assignmentsA)
            assert(result.assignmentsB == group.assignmentsB)
            assert(result.date == group.date)
        }
    }

    feature("adding a writer editor pair to an assigment") {
        scenario("A writer editor pair that is good") {
            val group: GroupAssignment = GroupAssignment()
            group.groupA.add("fred")

            group.groupB.add("barney")
            group.groupB.add("wilma")

            assert(group.assignmentsA.isEmpty)
            assert(group.assignmentsB.isEmpty)

            assert(false == group.addWriterToEditor("wilma", "betty"))

            assert(group.assignmentsA.isEmpty)
            assert(group.assignmentsB.isEmpty)

        }

        scenario("A writer editor pair where the editor does not exist") {
            val group: GroupAssignment = GroupAssignment()
            group.groupA.add("fred")

            group.groupB.add("barney")
            group.groupB.add("wilma")

            assert(group.assignmentsA.isEmpty)
            assert(group.assignmentsB.isEmpty)

            assert(false == group.addWriterToEditor("betty", "wilma"))

            assert(group.assignmentsA.isEmpty)
            assert(group.assignmentsB.isEmpty)


        }

        scenario("A writer editor pair where neither exists") {
            val group: GroupAssignment = GroupAssignment()
            group.groupA.add("fred")

            group.groupB.add("barney")
            group.groupB.add("wilma")

            assert(group.assignmentsA.isEmpty)
            assert(group.assignmentsB.isEmpty)

            assert(false == group.addWriterToEditor("missy", "jule"))

            assert(group.assignmentsA.isEmpty)
            assert(group.assignmentsB.isEmpty)

        }
    }

    feature("A copy can be made and altering it doesn't affect the first") {

        scenario("copy of an empty group") {
            val groupAssignment1: GroupAssignment = GroupAssignment()
            val groupAssignment2: GroupAssignment = groupAssignment1.copy

            groupAssignment2.groupA.add("fred")

            assert( ! groupAssignment1.groupA.contains("fred"))
            assert( groupAssignment2.groupA.contains("fred"))
        }

        scenario("copy of a filled out group") {
            val groupAssignment1: GroupAssignment = GroupAssignment()
            groupAssignment1.groupA.add("ted")

            val groupAssignment2: GroupAssignment = groupAssignment1.copy

            groupAssignment2.groupA.add("fred")

            assert( ! groupAssignment1.groupA.contains("fred"))
            assert( groupAssignment2.groupA.contains("fred"))
        }
    }

    feature("editorsPerWriter can be requested") {
        scenario("a group with assignments") {
            val text =
                """
                  |groupA: ["fred", "barney", "wilma"]
                  |groupB: ["betty", "kate", "bob", "ted", "ed"]
                  |assignmentsA: {"fred": ["betty", "kate"], "barney": ["bob", "ted"]}
                  |assignmentsB: {"betty": ["fred", "barney", "wilma"]}
                  |date:
                  |  year: 2017
                  |  month: 07
                  |  day: 17
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[GroupAssignment]))
            val groupAssignment = yaml.load(text).asInstanceOf[GroupAssignment]

            assert(groupAssignment.editorsPerWriterA().size == 3)
            assert(groupAssignment.editorsPerWriterA()("fred") == List("betty"))
            assert(groupAssignment.editorsPerWriterA()("barney") == List("betty"))
            assert(groupAssignment.editorsPerWriterA()("wilma") == List("betty"))

            assert(groupAssignment.editorsPerWriterB().size == 4)
            assert(groupAssignment.editorsPerWriterB()("betty") == List("fred"))
            assert(groupAssignment.editorsPerWriterB()("kate") == List("fred"))
            assert(groupAssignment.editorsPerWriterB()("bob") == List("barney"))
            assert(groupAssignment.editorsPerWriterB()("ted") == List("barney"))

        }

        scenario("a group with assignments made one at a time ") {
            val groupAssignment = GroupAssignment("1975-09-08")
            groupAssignment.groupA.add("fred")
            groupAssignment.groupA.add("bob")
            groupAssignment.groupB.add("kate")
            groupAssignment.groupB.add("wilma")

            assert(groupAssignment.editorsPerWriterA().isEmpty)
            assert(groupAssignment.editorsPerWriterB().isEmpty)
            groupAssignment.addWriterToEditor("fred", "kate")
            assert(groupAssignment.editorsPerWriterA().isEmpty)
            assert(groupAssignment.editorsPerWriterB()("kate") == List("fred"))
            groupAssignment.addWriterToEditor("bob", "wilma")
            assert(groupAssignment.editorsPerWriterA().isEmpty)
            assert(groupAssignment.editorsPerWriterB().size == 2)
            assert(groupAssignment.editorsPerWriterB()("kate") == List("fred"))
            assert(groupAssignment.editorsPerWriterB()("wilma") == List("bob"))
            groupAssignment.addWriterToEditor("kate", "bob")
            assert(groupAssignment.editorsPerWriterA().size == 1)
            assert(groupAssignment.editorsPerWriterA()("bob") == List("kate"))
            assert(groupAssignment.editorsPerWriterB().size == 2)
            assert(groupAssignment.editorsPerWriterB()("kate") == List("fred"))
            assert(groupAssignment.editorsPerWriterB()("wilma") == List("bob"))

        }
    }
}
