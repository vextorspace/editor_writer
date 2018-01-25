package com.ronnev.editorselection

import java.util

import org.scalatest.{FeatureSpec, GivenWhenThen}
import org.yaml.snakeyaml.{DumperOptions, Yaml}
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.nodes.Tag

class StudentTest extends FeatureSpec with GivenWhenThen {

    feature("A student loads and stores from a string") {
        scenario("A string of a student loads") {
            Given("A string of a student")

            val studentString =
                """name: Bob
                  |exclusions: [ted]
                  |history:
                  |- ["fred", "barney"]
                  |- ["wilma", "betty"]
                """.stripMargin

            When("loaded via YAML")

            val yaml = new Yaml(new Constructor(classOf[Student]))
            val result = yaml.load(studentString).asInstanceOf[Student]

            Then("The result is the student nicely filled out")

            assert(result.name == "Bob")
            assert(result.exclusions.size() == 1)
            assert(result.exclusions.get(0) == "ted")
            assert(result.history.size() == 2)
            val hist1 = result.history.get(0)
            val hist2 = result.history.get(1)
            assert(hist1.size() == 2)
            assert(hist1.get(0) == "fred")
            assert(hist1.get(1) == "barney")
            assert(hist2.size() == 2)
            assert(hist2.get(0) == "wilma")
            assert(hist2.get(1) == "betty")
        }

        scenario("A Student storing to string") {
            Given("A Student")

            val student = new Student()
            student.name = "bob"
            student.exclusions = new util.ArrayList[String]()
            student.exclusions.add("kate")
            student.exclusions.add("teddy bear")
            student.history = new util.ArrayList[util.List[String]]()
            val hist1 = new util.ArrayList[String]()
            hist1.add("fred")
            hist1.add("barney")
            student.history.add(hist1)

            When("it is written to a string")
            val yaml = new Yaml()
            val result = yaml.dumpAs(student, Tag.MAP, DumperOptions.FlowStyle.AUTO)

            Then("It is properly formatted")
            assert(result.lines.map(_.trim).toList.sorted == List("name: bob", "exclusions: [kate, teddy bear]", "history:","- [fred, barney]").sorted)

        }

        scenario("A student written and read back") {
            Given("A Student")

            val student = new Student()
            student.name = "bob"
            student.exclusions = new util.ArrayList[String]()
            student.exclusions.add("kate")
            student.exclusions.add("teddy bear")
            student.history = new util.ArrayList[util.List[String]]()
            val hist1 = new util.ArrayList[String]()
            hist1.add("fred")
            hist1.add("barney")
            student.history.add(hist1)

            When("It is written to a string and read back")

            val yaml = new Yaml()
            val studentString = yaml.dumpAs(student, Tag.MAP, DumperOptions.FlowStyle.AUTO)
            val yaml2 = new Yaml(new Constructor(classOf[Student]))
            val result = yaml2.load(studentString).asInstanceOf[Student]

            Then("the fields are the same")

            assert(student.name == result.name)
            assert(student.history == result.history)
            assert(student.exclusions == result.exclusions)
        }

        scenario("A mostly empty student written and read back") {
            Given("A Student")

            val student = new Student()
            student.name = "bob"
            student.exclusions = new util.ArrayList[String]()
            student.history = new util.ArrayList[util.List[String]]()

            When("It is written to a string and read back")

            val yaml = new Yaml()
            val studentString = yaml.dumpAs(student, Tag.MAP, DumperOptions.FlowStyle.AUTO)
            val yaml2 = new Yaml(new Constructor(classOf[Student]))
            val result = yaml2.load(studentString).asInstanceOf[Student]

            Then("the fields are the same")

            assert(student.name == result.name)
            assert(student.history == result.history)
            assert(student.exclusions == result.exclusions)

        }
    }
}
