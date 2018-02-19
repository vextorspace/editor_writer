package com.ronnev.editorselection.ui

import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.control.{Label, ListView}
import javafx.scene.layout.VBox

import com.ronnev.editorselection.StudentChangedListener

class GroupsView extends VBox with GroupsDisplay with StudentChangedListener {
    private val groupAListView = new ListView[String]()
    private val groupAModel: ObservableList[String] = FXCollections.observableArrayList()
    private val groupBListView = new ListView[String]()
    private val groupBModel: ObservableList[String] = FXCollections.observableArrayList()
    private val exclusionsListView = new ListView[String]()
    private val exclusionsModel: ObservableList[String] = FXCollections.observableArrayList()


    groupAListView.setEditable(false)
    groupAListView.setItems(groupAModel)
    groupBListView.setEditable(false)
    groupBListView.setItems(groupBModel)

    exclusionsListView.setEditable(false)
    exclusionsListView.setItems(exclusionsModel)

    getChildren.addAll(new Label("Group A:"), groupAListView, new Label("Group B:"), groupBListView, new Label("Exclusions:"), exclusionsListView)

    override def displayGroupA(students: List[String]): Unit = {
        groupAModel.clear()
        students.foreach(groupAModel.add(_))
    }

    override def addGroupA(student: String): Unit = groupAModel.add(student)

    override def displayGroupB(students: List[String]): Unit = {
        groupBModel.clear()
        students.foreach(groupBModel.add(_))
    }

    override def addGroupB(student: String): Unit = groupBModel.add(student)

    override def displayExclusions(studentPairs: List[(String, String)]): Unit = {
        exclusionsModel.clear()
        studentPairs.foreach(pair => exclusionsModel.add(s"${pair._1} || ${pair._2}"))
    }

    override def addExclusion(studentPair: (String, String)): Unit = exclusionsModel.add(s"${studentPair._1} || ${studentPair._2}")

    override def clear(): Unit = {
        groupAModel.clear()
        groupBModel.clear()
        exclusionsModel.clear()
    }

    override def onAdded(student: String): Unit = {}

    override def onRemoved(student: String): Unit = {
        groupAModel.remove(student)
        groupBModel.remove(student)
        exclusionsModel.removeIf(exclusion => {
            val students = exclusion.split('|')
            if (students.length != 3)
                return true
            students(0).trim == student || students(2).trim() == student
        })
    }
}

object GroupsView {
    def apply() = new GroupsView()
}