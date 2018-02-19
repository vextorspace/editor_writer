package com.ronnev.editorselection

import collection.JavaConverters._
import com.ronnev.editorselection.assignment.GroupAssignment
import com.ronnev.editorselection.dates.SimpleDate
import org.scalatest.FeatureSpec
import org.yaml.snakeyaml.{DumperOptions, Yaml}
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.nodes.Tag

class SchoolClassSpec extends FeatureSpec {
    feature("A SchoolClass loads from text") {
        scenario(("an empty class")) {
            val text =
                """
                  |students: []
                  |groupA: []
                  |groupB: []
                  |history: []
                  |exclusions: []
                  |editorsPerWriter: 3
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[SchoolClass]))
            val schoolClass = yaml.load(text).asInstanceOf[SchoolClass]

            assert(schoolClass.history.isEmpty)
            assert(schoolClass.students.isEmpty)
        }

        scenario("A class with some students") {
            val text =
                """
                  |students: ["fred", "wilma", "betty"]
                  |groupA: ["fred"]
                  |groupB: ["wilma", "betty"]
                  |history: []
                  |exclusions: []
                  |editorsPerWriter: 3
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[SchoolClass]))
            val schoolClass = yaml.load(text).asInstanceOf[SchoolClass]

            assert(schoolClass.history.isEmpty)
            assert(schoolClass.students.toArray.toList == List("fred", "wilma", "betty"))
        }

        scenario("A class with some students and history") {
            val text =
                """
                  |students: ["fred", "barney", "wilma", "betty"]
                  |groupA: ["fred", "barney"]
                  |groupB: ["wilma", "betty"]
                  |exclusions: []
                  |history:
                  |  - date: {year: 2010, month: 07, day: 17}
                  |    groupA: ["fred", "barney"]
                  |    groupB: ["wilma", "betty"]
                  |    assignmentsA:
                  |      fred: ["wilma", "betty"]
                  |      barney: []
                  |    assignmentsB:
                  |      wilma: ["fred"]
                  |      betty: ["barney"]
                  |  - date: {year: 2017, month: 07, day: 17}
                  |    groupA: ["fred", "barney"]
                  |    groupB: ["wilma", "betty"]
                  |    assignmentsA:
                  |      barney: ["wilma", "betty"]
                  |      fred: []
                  |    assignmentsB:
                  |      wilma: ["barney"]
                  |      betty: ["fred"]
                  |editorsPerWriter: 3
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[SchoolClass]))
            val schoolClass = yaml.load(text).asInstanceOf[SchoolClass]

            assert(schoolClass.students.toArray.toList == List("fred", "barney", "wilma", "betty"))

            assert(schoolClass.history.get(0).date == SimpleDate("2010-07-17").get)
            assert(schoolClass.history.get(0).groupA.toArray.toList == List("fred", "barney"))
            assert(schoolClass.history.get(0).groupB.toArray.toList == List("wilma", "betty"))
            assert(schoolClass.history.get(0).assignmentsA.get("barney").isEmpty)
            assert(schoolClass.history.get(0).assignmentsA.get("fred").toArray.toList == List("wilma", "betty"))
            assert(schoolClass.history.get(0).assignmentsB.get("wilma").toArray.toList == List("fred"))
            assert(schoolClass.history.get(0).assignmentsB.get("betty").toArray.toList == List("barney"))

            assert(schoolClass.history.get(1).date == SimpleDate("2017-07-17").get)
            assert(schoolClass.history.get(1).groupA.toArray.toList == List("fred", "barney"))
            assert(schoolClass.history.get(1).groupB.toArray.toList == List("wilma", "betty"))
            assert(schoolClass.history.get(1).assignmentsA.get("fred").isEmpty)
            assert(schoolClass.history.get(1).assignmentsA.get("barney").toArray.toList == List("wilma", "betty"))
            assert(schoolClass.history.get(1).assignmentsB.get("betty").toArray.toList == List("fred"))
            assert(schoolClass.history.get(1).assignmentsB.get("wilma").toArray.toList == List("barney"))
        }
    }

    feature("History GroupAssignments are kept in date order") {
        scenario("Adding an earlier group assignment puts it before in the list") {
            val text =
                """
                  |students: ["fred", "barney", "wilma", "betty"]
                  |groupA: ["fred", "barney"]
                  |groupB: ["wilma", "betty"]
                  |exclusions: []
                  |history:
                  |  - date: {year: 2010, month: 07, day: 17}
                  |    groupA: ["fred", "barney"]
                  |    groupB: ["wilma", "betty"]
                  |    assignmentsA:
                  |      fred: ["wilma", "betty"]
                  |      barney: []
                  |    assignmentsB:
                  |      wilma: ["fred"]
                  |      betty: ["barney"]
                  |editorsPerWriter: 3
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[SchoolClass]))
            val schoolClass = yaml.load(text).asInstanceOf[SchoolClass]
            val newAssignment = GroupAssignment("2009-08-12")

            schoolClass.acceptGroupAssignment(newAssignment)

            assert(schoolClass.history.get(0).date == SimpleDate("2009-08-12").get)
        }

        scenario("adding a later group assignment puts it later in the list") {
            val text =
                """
                  |students: ["fred", "barney", "wilma", "betty"]
                  |groupA: ["fred", "barney"]
                  |groupB: ["wilma", "betty"]
                  |exclusions: []
                  |history:
                  |  - date: {year: 2010, month: 07, day: 17}
                  |    groupA: ["fred", "barney"]
                  |    groupB: ["wilma", "betty"]
                  |    assignmentsA:
                  |      fred: ["wilma", "betty"]
                  |      barney: []
                  |    assignmentsB:
                  |      wilma: ["fred"]
                  |      betty: ["barney"]
                  |editorsPerWriter: 3
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[SchoolClass]))
            val schoolClass = yaml.load(text).asInstanceOf[SchoolClass]
            val newAssignment = GroupAssignment("2010-07-20")

            schoolClass.acceptGroupAssignment(newAssignment)

            assert(schoolClass.history.get(1).date == SimpleDate("2010-07-20").get)
        }

        scenario("adding a group assignment of the same date replaces it") {
            val text =
                """
                  |students: ["fred", "barney", "wilma", "betty"]
                  |groupA: ["fred", "barney"]
                  |groupB: ["wilma", "betty"]
                  |exclusions: []
                  |history:
                  |  - date: {year: 2010, month: 07, day: 17}
                  |    groupA: ["fred", "barney"]
                  |    groupB: ["wilma", "betty"]
                  |    assignmentsA:
                  |      fred: ["wilma", "betty"]
                  |      barney: []
                  |    assignmentsB:
                  |      wilma: ["fred"]
                  |      betty: ["barney"]
                  |editorsPerWriter: 3
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[SchoolClass]))
            val schoolClass = yaml.load(text).asInstanceOf[SchoolClass]
            val newAssignment = GroupAssignment("2010-07-17")

            schoolClass.acceptGroupAssignment(newAssignment)

            assert(schoolClass.history.size() == 1)
            assert(schoolClass.history.get(0).groupA.isEmpty)
            assert(schoolClass.history.get(0).groupB.isEmpty)
            assert(schoolClass.history.get(0).assignmentsA.isEmpty)
            assert(schoolClass.history.get(0).assignmentsB.isEmpty)
        }
    }

    feature("A schoolclass saves and loads back to the same thing") {
        scenario("A fully filled out schoolclass") {
            val schoolClass = SchoolClass()
            schoolClass.addStudent("fred")
            schoolClass.addStudent("bob")
            schoolClass.addStudent("kate")
            schoolClass.addStudent("wilma")
            schoolClass.addStudentToGroupA("fred")
            schoolClass.addStudentToGroupA("bob")
            schoolClass.addStudentToGroupB("kate")
            schoolClass.addStudentToGroupB("wilma")
            schoolClass.addExclusion("bob", "kate")

            var groupAssignment = schoolClass.makeEmptyNewAssigmnent(SimpleDate("1975-09-08").get)

            groupAssignment.addWriterToEditor("fred", "wilma")
            groupAssignment.addWriterToEditor("fred", "kate")
            groupAssignment.addWriterToEditor("kate", "fred")
            schoolClass.acceptGroupAssignment(groupAssignment)

            groupAssignment = schoolClass.makeEmptyNewAssigmnent(SimpleDate("2010-07-17").get)
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

            schoolClass.acceptGroupAssignment(groupAssignment)

            val yaml = new Yaml()
            val groupString = yaml.dumpAs(schoolClass, Tag.MAP, DumperOptions.FlowStyle.AUTO)

            println(groupString)

            val yaml2 = new Yaml(new Constructor(classOf[SchoolClass]))
            val result = yaml2.load(groupString).asInstanceOf[SchoolClass]

            assert(result.students.asScala.toList.sorted == List("fred", "bob", "kate", "wilma").sorted)
            assert(result.exclusions.size() == 1)
            assert(result.exclusions.get(0).asScala.toList.sorted == List("bob", "kate").sorted)
            assert(result.history.size() == 2)
            assert(result.groupA.asScala.toList.sorted == List("fred", "bob").sorted)
            assert(result.groupB.asScala.toList.sorted == List("kate", "wilma").sorted)

            val firstHistory: GroupAssignment = result.history.get(0)
            val secondHistory: GroupAssignment = result.history.get(1)

            assert(firstHistory.date.compareTo(SimpleDate("1975-09-08").get) == 0)
            assert(firstHistory.groupA.asScala.toList.sorted == List("fred", "bob").sorted)
            assert(firstHistory.groupB.asScala.toList.sorted == List("kate", "wilma").sorted)
            assert(firstHistory.editorsPerWriterA()("fred") == List("kate"))

            assert(firstHistory.editorsPerWriterA()("fred") == List("kate"))
            assert(firstHistory.editorsPerWriterB()("wilma") == List("fred"))
            assert(firstHistory.editorsPerWriterB()("kate") == List("fred"))

            assert(secondHistory.date.compareTo(SimpleDate("2010-07-17").get) == 0)
            assert(secondHistory.groupA.asScala.toList.sorted == List("fred", "bob").sorted)
            assert(secondHistory.groupB.asScala.toList.sorted == List("kate", "wilma").sorted)

            assert(secondHistory.editorsPerWriterA()("bob") == List("kate"))
            assert(secondHistory.editorsPerWriterB()("wilma") == List("bob"))
            assert(secondHistory.editorsPerWriterB()("kate") == List("fred"))

        }
    }

    feature("exclusions can be added and removed") {
        scenario("an empty schoolclass") {
            val schoolClass = SchoolClass()

            schoolClass.addStudents("Fred", "Wilma", "Betty")

            assert(schoolClass.exclusions.isEmpty)

            schoolClass.addExclusion("Fred", "Wilma")

            assert(schoolClass.exclusions.size() == 1)

            assert(schoolClass.exclusions.get(0).asScala.toList.sorted == List("Fred", "Wilma").sorted)

            schoolClass.removeExclusion("Fred", "Wilma")

            assert(schoolClass.exclusions.isEmpty)

        }
    }
}
