package com.ronnev.editorselection

import java.util

import scala.beans.BeanProperty

class Student {
    @BeanProperty var name: String = ""
    @BeanProperty var exclusions: util.List[String] = new util.ArrayList[String]()
    @BeanProperty var history: util.List[util.List[String]] = new util.ArrayList[util.List[String]]()
}

object Student {
    def apply(name: String) : Student = {
        val stud = new Student()
        stud.name = name
        stud
    }
}