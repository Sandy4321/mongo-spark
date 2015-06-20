/*
 * ScalaWordCount.scala
 * Written in 2014 by Sampo Niskanen / Mobile Wellness Solutions MWS Ltd
 * 
 * To the extent possible under law, the author(s) have dedicated all copyright and
 * related and neighboring rights to this software to the public domain worldwide.
 * This software is distributed without any warranty.
 * 
 * See <http://creativecommons.org/publicdomain/zero/1.0/> for full details.
 */


package mongo_spark

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.hadoop.conf.Configuration
import org.bson.BSONObject
import org.bson.BasicBSONObject
import org.bson.BSONObject
import mongo_spark.MongoWrite._
import mongo_spark.MongoRead._

object ScalaWordCount {

  def main(args: Array[String]) {
    if (args.length < 5) {
      System.err.println("Usage: <mongo-host:port> <DB_NAME.INPUT_COLLECTION> <DB_NAME.OUTPUT_COLLECTION> <TEXT_FIELD_NAME_TO_COUNT_WORDS>")
      System.err.println("Example: 127.0.0.1:27017 test.testData_in test.testData_out text")
      System.exit(-1)
    }
    val sparkConf = new SparkConf()
    val sc = new SparkContext(sparkConf)
    val config = new Configuration()
    val mongoRDD = readBsonRDD("mongodb://" + args(0) + "/" + args(1), sc, "mongodb")

    val countsRDD = mongoRDD.flatMap(arg => {
      var str = arg.get(args(3)).toString
      str = str.toLowerCase().replaceAll("[.,!?\n]", " ")
      str.split(" ")
    })
      .map(word => (word, 1))
      .reduceByKey((a, b) => a + b)

    val saveRDD = countsRDD.map((tuple) => {
      var bson = new BasicBSONObject()
      bson.put("word", tuple._1)
      bson.put("count", tuple._2)
      bson
    })

    writeBsonRDD[(Object, org.bson.BSONObject)](saveRDD.map(x => (null, x)), path = "mongodb://" + args(1) + "/" + args(1), format = "mongodb")
    
  }
}