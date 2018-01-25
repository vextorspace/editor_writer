package com.ronnev.editorselection

import org.scalatest.FeatureSpec
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

class GroupAssignmentTest extends FeatureSpec {

    feature("A GroupAssignment loads from text") {
        scenario("An empty text makes an empty group assignment") {

            val text =
                """
                  |groupA: []
                  |groupB: []
                  |assignmentsA: {}
                  |assignmentsB: {}
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[GroupAssignment]))
            val result = yaml.load(text).asInstanceOf[GroupAssignment]

            assert(result.assignmentsA.isEmpty)
            assert(result.assignmentsB.isEmpty)
            assert(result.groupA.isEmpty)
            assert(result.groupB.isEmpty)
        }

        scenario("An empty text makes an empty group assignment") {

            val text =
                """
                  |groupA: ["fred", "barney", "wilma"]
                  |groupB: ["betty", "kate", "bob", "ted", "ed"]
                  |assignmentsA: {"fred": ["betty", "kate"], "barney": ["bob", "ted"]}
                  |assignmentsB: {"betty": ["fred", "barney", "wilma"]}
                """.stripMargin

            val yaml = new Yaml(new Constructor(classOf[GroupAssignment]))
            val result = yaml.load(text).asInstanceOf[GroupAssignment]

            assert(result.assignmentsA.size() == 2)
            assert(result.assignmentsA.get("fred").toList == List("betty", "kate"))
            assert(result.assignmentsA.get("barney").toList == List("bob", "ted"))
            assert(result.assignmentsB.isEmpty)
            assert(result.groupA.isEmpty)
            assert(result.groupB.isEmpty)
        }

    }

}
