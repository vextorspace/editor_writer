package com.ronnev.editorselection

import javafx.application.Application
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.Scene
import javafx.scene.control.{Menu, MenuBar, MenuItem, TextArea}
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage

class EditorSelection extends Application {

    override def start(primaryStage: Stage): Unit = {
        primaryStage.setTitle("Messages");
        val mainBox = new VBox()
        val scene = new Scene(mainBox, 400, 350);
        scene.setFill(Color.OLDLACE);

        val textArea = new TextArea("here")
        textArea.setEditable(false)


        val menuBar = new MenuBar()
        val fileMenu = new Menu("File")
        val editMenu = new Menu("Edit")

        val loadRosterMenuItem = new MenuItem("Load Roster")
        loadRosterMenuItem.setOnAction(event => {
            textArea.appendText("boo")
        })
        fileMenu.getItems.add(loadRosterMenuItem)
        menuBar.getMenus.addAll(fileMenu, editMenu)

        scene.getRoot().asInstanceOf[VBox].getChildren().addAll(menuBar)


        mainBox.getChildren.add(textArea)

        primaryStage.setScene(scene)

        primaryStage.toBack()

        primaryStage.show()
    }

    def launchMe() : Unit = Application.launch()
}

object EditorSelection {
    def apply() : EditorSelection = new EditorSelection()

    def main(args: Array[String]) : Unit = {
        EditorSelection().launchMe()
    }
}