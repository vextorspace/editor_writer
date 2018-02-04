package com.ronnev.editorselection.files
import java.io.{File, FileWriter}

import com.ronnev.editorselection.SchoolClass
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.{DumperOptions, Yaml}

import scala.util.Try

object ActualFileSaver extends FileSaver {
    override def saveFile(schoolClass: SchoolClass, file: File): Boolean = {

        val groupString = new Yaml().dumpAs(schoolClass, Tag.MAP, DumperOptions.FlowStyle.AUTO)
        val writer = Try(new FileWriter(file))
        writer.map(w => {w.write(groupString); w}).recoverWith{case e => {e.printStackTrace(); writer}}.map(_.close)

        !writer.isFailure
    }

    override def loadFile(file: File): Try[SchoolClass] = {

        val source = scala.io.Source.fromFile(file)
        val lines = try source.mkString finally source.close()

        val yaml = new Yaml(new Constructor(classOf[SchoolClass]))

        Try(yaml.load(lines).asInstanceOf[SchoolClass])
    }
}
