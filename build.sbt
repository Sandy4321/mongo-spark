import AssemblyKeys._

assemblySettings

name := "mongo-spark"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += 	"org.apache.spark" %% "spark-core" % "1.0.0"  % "provided"

libraryDependencies += 	"org.apache.hadoop" % "hadoop-client" % "1.0.4"

libraryDependencies += 	"org.mongodb" % "mongo-java-driver" % "2.12.2"

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
	{
		case PathList("org", "apache", "commons", xs @ _*)         => MergeStrategy.first
		case x => old(x)
	} 
}

retrieveManaged := true

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"


