package com.ronnev.editorselection

import com.ronnev.editorselection.assignment.GroupAssignment
import org.scalatest.FeatureSpec
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

class SchoolClassSpec extends FeatureSpec {
    feature("A SchoolClass loads from text") {
        scenario(("an empty class")) {
            val text =
                """
                  |students: []
                  |groupA: []
                  |groupB: []
                  |history: []
                  |maxWritersPerEditor: 3
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[SchoolClass]))
            val schoolClass = yaml.load(text).asInstanceOf[SchoolClass]

            assert(schoolClass.history.isEmpty)
            assert(schoolClass.students.isEmpty)
            assert(schoolClass.maxWritersPerEditor == 3)
        }

        scenario("A class with some students") {
            val text =
                """
                  |students: ["fred", "wilma", "betty"]
                  |groupA: ["fred"]
                  |groupB: ["wilma", "betty"]
                  |history: []
                  |maxWritersPerEditor: 2
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[SchoolClass]))
            val schoolClass = yaml.load(text).asInstanceOf[SchoolClass]

            assert(schoolClass.history.isEmpty)
            assert(schoolClass.students.toArray.toList == List("fred", "wilma", "betty"))
            assert(schoolClass.maxWritersPerEditor == 2)
        }

        scenario("A class with some students and history") {
            val text =
                """
                  |students: ["fred", "barney", "wilma", "betty"]
                  |groupA: ["fred", "barney"]
                  |groupB: ["wilma", "betty"]
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
                  |maxWritersPerEditor: 3
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[SchoolClass]))
            val schoolClass = yaml.load(text).asInstanceOf[SchoolClass]

            assert(schoolClass.students.toArray.toList == List("fred", "barney", "wilma", "betty"))

            assert(schoolClass.maxWritersPerEditor == 3)
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
                  |maxWritersPerEditor: 3
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
                  |maxWritersPerEditor: 3
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
                  |maxWritersPerEditor: 3
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
}
