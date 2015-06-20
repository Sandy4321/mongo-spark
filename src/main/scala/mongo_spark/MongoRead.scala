package mongo_spark

import org.apache.spark.SparkContext._
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.rdd.RDD
import org.apache.hadoop.conf.Configuration
import org.bson.BSONObject
import com.mongodb.hadoop.MongoInputFormat
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import com.mongodb.hadoop.BSONFileInputFormat

object MongoRead {
  def readBsonRDD(path: String, sc: SparkContext, format: String = "mongodb") = {

    val rdd =
      format match {
        case "serialized_file" => {
          sc.objectFile[BSONObject](path = path)
        }
        case "mongodb" => {
          val config = new Configuration()
          config.set("mongo.input.uri", path)
          config.set("mongo.input.split_size", "256")
          sc.newAPIHadoopRDD(config, classOf[com.mongodb.hadoop.MongoInputFormat], classOf[Object], classOf[BSONObject])
            // take only bson object
            .map(arg => arg._2)
        }
        case "bson" => {
          val config = new Configuration()
          config.set("mongo.job.input.format", "com.mongodb.hadoop.BSONFileInputFormat")
          sc.newAPIHadoopFile(path = path,
            fClass = classOf[BSONFileInputFormat].asSubclass(classOf[FileInputFormat[Object, BSONObject]]),
            kClass = classOf[Object],
            vClass = classOf[BSONObject],
            conf = config)
            // take only bson object
            .map(arg => arg._2)
        }
      }
    rdd
  }
}