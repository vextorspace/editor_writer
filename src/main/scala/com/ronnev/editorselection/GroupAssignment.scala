package com.ronnev.editorselection

import java.util

import scala.beans.BeanProperty

class GroupAssignment {
    @BeanProperty var groupA: java.util.List[String] = new util.ArrayList[String]()
    @BeanProperty var groupB: java.util.List[String] = new util.ArrayList[String]()
    @BeanProperty var assignmentsA: java.util.Map[String, String] = new util.HashMap[String, String]()
    @BeanProperty var assignmentsB: java.util.Map[String, String] = new util.HashMap[String, String]()

}

object GroupAssignment {
    def apply() = new GroupAssignment()
}