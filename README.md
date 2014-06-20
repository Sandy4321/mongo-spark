mongo-spark
===========

Example application on how to use [mongo-hadoop][1] connector with [Apache Spark][2].

[1]: https://github.com/mongodb/mongo-hadoop
[2]: https://spark.incubator.apache.org/


Prerequisites
-------------

* MongoDB installed and running on local or remote machine
* Scala 2.10 and SBT installed
* [Sbt assemly plugin](https://github.com/sbt/sbt-assembly) installed
* [Apache spark 1.0](http://spark.apache.org/docs/1.0.0/index.html) installed

How to
-------

First of all thanks to [original repository](https://github.com/plaa/mongo-spark) and [blog post](http://codeforhire.com/2014/02/18/using-spark-with-mongodb/).
This example provides instructions on how to run you application on [stadalone spark cluster running on ec2](http://spark.apache.org/docs/1.0.0/ec2-scripts.html).  
1. Because by default ec2 scipt installs standalone spark cluster with **hadoop version 1.0.4** you should build you mongo-hadoop connector against this version of hadoop (here the prebuilded version is located at *lib/*). Please check [mongo-hadoop-conector repository](https://github.com/mongodb/mongo-hadoop) to learn how to build mongo-hadoop connector against your version of hadoop.
2. The strightforward way to run your application is to make "fat" jar using assembly sbt plugin:  
    **`sbt assembly`**  
and pass it to [submit script](http://spark.apache.org/docs/latest/submitting-applications.html). Please review the **[build.sbt](https://github.com/dselivanov/mongo-spark/blob/master/build.sbt)** file and check **mergeStrategy** and **libraryDependencies**.  
3. **Copy** your "fat jar" ( mongo-spark-assembly-1.0.jar ) to **spark master**:  
 `scp mongo-spark-assembly-1.0.jar root@hostSparkMaster:/root/jobs/`

Running
-------

Generate sample data (or use existing), run `ScalaWordCount` and print the results. For example you can add some data using *mongo* shell

`for (var i = 1; i <= 25; i++) db.INPUT_COLLECTION.insert( { TEXT_FIELD_NAME_TO_COUNT_WORDS : "bla blaa blaaa" } )`
    
1. Run your job on spark master:  
  `~/spark/bin/spark-submit --class ScalaWordCount --master spark://hostSparkMaster:7077 ~/jobs/mongo-spark-assembly-1.0.jar <mongo-host:port> <DB_NAME.INPUT_COLLECTION> <DB_NAME.OUTPUT_COLLECTION> <TEXT_FIELD_NAME_TO_COUNT_WORDS>`  

1. check result in DB_NAME.OUTPUT_COLLECTION


License
-------

The code itself is released to the public domain according to the [Creative Commons CC0][3].
