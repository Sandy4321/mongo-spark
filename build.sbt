name := "mongo-spark"

version := "0.2"

scalaVersion := "2.10.4"

libraryDependencies += 	"org.apache.spark" %% "spark-core" % "1.4.0" % "provided"

libraryDependencies += 	"org.mongodb" % "mongo-java-driver" % "2.12.4"

libraryDependencies += "org.mongodb.mongo-hadoop" % "mongo-hadoop-core" % "1.3.2"

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
	{
		case PathList("org", "apache", "commons", xs @ _*)         => MergeStrategy.first
		case PathList("org", "apache", "hadoop", xs @ _*)         => MergeStrategy.first
		case PathList("org", "apache", "hadoop", xs @ _*)         => MergeStrategy.first
		case x => old(x)
	} 
}


retrieveManaged := true

resolvers ++= Seq(
	"Akka Repository" at "http://repo.akka.io/releases/"
)
