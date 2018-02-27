
name := "editorselection"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies += "org.yaml" % "snakeyaml" % "1.19"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"
libraryDependencies += "com.itextpdf" % "itextpdf" % "5.5.13"

mainClass in assembly := Some("com.ronnev.editorselection.EditorSelection")

test in assembly := {}