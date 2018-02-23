package com.ronnev.editorselection.assignment.restrictions

import org.scalatest.FeatureSpec

class BadPairRestrictionTest extends FeatureSpec {
    feature("bad pair restricton checks if the pair exists either direction in the assignment") {
        scenario("restriction between fred and wilma, wilma in editors") {
            val restriction = BadPairRestriction("fred", "wilma")

            val result1 = restriction.goodCombo("fred", List("betty", "wilma"))

            assert(result1 == false)

            val result2 = restriction.goodCombo("wilma", List("betty", "fred"))

            assert(result2 == false)
        }

        scenario("restriction between fred and wilma, wilma not editors") {
            val restriction = BadPairRestriction("fred", "wilma")

            val result = restriction.goodCombo("fred", List("betty", "barney"))

            assert(result == true)
        }

        scenario("restriction between fred and wilma, empty list") {
            val restriction = BadPairRestriction("fred", "wilma")

            val result = restriction.goodCombo("fred", List.empty)

            assert(result == true)
        }

        scenario("restriction between fred and wilma, fred not writer") {
            val restriction = BadPairRestriction("fred", "wilma")

            val result = restriction.goodCombo("ed", List("wilma", "barney"))

            assert(result == true)
        }


    }
}
